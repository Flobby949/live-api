package top.flobby.live.gift.provider.consumer;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.util.ObjectUtils;
import top.flobby.live.bank.dto.AccountTradeDTO;
import top.flobby.live.bank.interfaces.ICurrencyAccountRpc;
import top.flobby.live.bank.vo.AccountTradeVO;
import top.flobby.live.common.constants.GiftProviderTopicNamesConstant;
import top.flobby.live.framework.mq.starter.properties.RocketMQConsumerProperties;
import top.flobby.live.framework.redis.starter.key.GiftProviderCacheKeyBuilder;
import top.flobby.live.gift.constant.SendGiftRoomTypeEnum;
import top.flobby.live.gift.dto.SendGiftMqDTO;
import top.flobby.live.im.common.AppIdEnum;
import top.flobby.live.im.dto.ImMsgBody;
import top.flobby.live.im.router.constants.ImMsgBizCodeEnum;
import top.flobby.live.im.router.interfaces.ImRouterRpc;
import top.flobby.live.living.dto.LivingRoomReqDTO;
import top.flobby.live.living.interfaces.ILivingRoomRpc;
import top.flobby.live.living.vo.LivingRoomInfoVO;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 发送礼物消费者
 * @create : 2023-12-18 14:27
 **/

@Slf4j
@Configuration
public class SendGiftConsumer implements InitializingBean {

    @Resource
    private RocketMQConsumerProperties rocketMQConsumerProperties;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private GiftProviderCacheKeyBuilder cacheKeyBuilder;
    @DubboReference
    private ICurrencyAccountRpc currencyAccountRpc;
    @DubboReference
    private ImRouterRpc routerRpc;
    @DubboReference
    private ILivingRoomRpc livingRoomRpc;

    /**
     * PK 最大数值
     */
    public static final Long PK_MAX_NUM = 100L;
    /**
     * PK 最大数值
     */
    public static final Long PK_MIN_NUM = 0L;
    /**
     * PK 初始
     */
    public static final Long PK_INIT_NUM = 50L;

    /**
     * Lua 脚本,用于更新进度条
     */
    private final String LUA_SCRIPT =
            "if (redis.call('exists', KEYS[1])) == 1 then " +
                    " local currentNum=redis.call('get',KEYS[1]) " +
                    " if (tonumber(currentNum)<=tonumber(ARGV[2]) and tonumber(currentNum)>=tonumber(ARGV[3])) then " +
                    " return redis.call('incrby',KEYS[1],tonumber(ARGV[4])) " +
                    " else return currentNum end " +
                    "else " +
                    "redis.call('set', KEYS[1], tonumber(ARGV[1])) " +
                    "redis.call('EXPIRE', KEYS[1], 3600 * 12) " +
                    "return ARGV[1] end";

    @Override
    public void afterPropertiesSet() throws Exception {
        DefaultMQPushConsumer mqPushConsumer = new DefaultMQPushConsumer();
        // 老版本中会开启，新版本的mq不需要使用到
        mqPushConsumer.setVipChannelEnabled(false);
        mqPushConsumer.setNamesrvAddr(rocketMQConsumerProperties.getNameSrv());
        mqPushConsumer.setConsumerGroup(rocketMQConsumerProperties.getGroupName() + "_" + SendGiftConsumer.class.getSimpleName());
        // 一次从broker中拉取10条消息到本地内存当中进行消费
        /* 但是！如果十条中出现异常，就会消费失败，那么这十条消息就会被重新消费
         * 为了防止礼物被重复消费，需要在消息体的DTO中添加一个字段，用来标识消息是否被消费过
         */
        mqPushConsumer.setConsumeMessageBatchMaxSize(10);
        mqPushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        // 监听礼物缓存数据更新的行为
        mqPushConsumer.subscribe(GiftProviderTopicNamesConstant.SEND_GIFT, "");
        mqPushConsumer.setMessageListener((MessageListenerConcurrently) (msgs, context) -> {
            for (MessageExt msg : msgs) {
                SendGiftMqDTO sendGiftMqDTO = JSON.parseObject(new String(msg.getBody()), SendGiftMqDTO.class);
                log.info("[SendGiftConsumer] send gift msg is {}", sendGiftMqDTO);
                String mqConsumeKey = cacheKeyBuilder.buildGiftConsumeKey(sendGiftMqDTO.getUuid());
                // 将消息标记为已消费
                Boolean lockStatus = redisTemplate.opsForValue().setIfAbsent(mqConsumeKey, -1, 5, TimeUnit.MINUTES);
                if (Boolean.FALSE.equals(lockStatus)) {
                    // 代表消息已经被消费过了
                    continue;
                }
                AccountTradeDTO accountTradeDTO = AccountTradeDTO.builder()
                        .userId(sendGiftMqDTO.getUserId())
                        .tradeAmount(sendGiftMqDTO.getPrice())
                        .build();
                // 虽然可能会出现系统异常导致余额扣减失败，但是消息已经被标记为已消费，下次拉取时不会重复消费
                // 但是在用户看来，余额并没有扣减，对用户没有操作损失
                AccountTradeVO result = currencyAccountRpc.consumeForSendGift(accountTradeDTO);
                // 如果余额扣减成功
                JSONObject msgDataBody = new JSONObject();
                Integer sendGiftType = sendGiftMqDTO.getType();
                if (result.isOperationSuccess()) {
                    // 触发礼物特效推送
                    Integer roomId = sendGiftMqDTO.getRoomId();
                    LivingRoomReqDTO livingRoomReqDTO = new LivingRoomReqDTO();
                    livingRoomReqDTO.setId(roomId);
                    livingRoomReqDTO.setAppId(AppIdEnum.LIVE_BIZ_ID.getCode());
                    List<Long> userIdList = livingRoomRpc.queryUserIdByRoomId(livingRoomReqDTO);
                    msgDataBody.put("url", sendGiftMqDTO.getSvgaUrl());
                    // 按类型群发
                    if (SendGiftRoomTypeEnum.PK_SEND_GIFT_ROOM.getCode().equals(sendGiftType)) {
                        sendPkImMsg(roomId, sendGiftMqDTO, msgDataBody, userIdList);
                    } else {
                        batchSendImMsg(userIdList, ImMsgBizCodeEnum.LIVING_ROOM_IM_GIFT_SUCCESS_MSG.getCode(), msgDataBody);
                    }
                } else {
                    // 利用IM消息，发送失败消息给用户
                    msgDataBody.put("msg", result.getMessage());
                    sendImMsgSingleton(sendGiftMqDTO.getUserId(), ImMsgBizCodeEnum.LIVING_ROOM_IM_GIFT_FAIL_MSG.getCode(), msgDataBody);
                }
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        mqPushConsumer.start();
        log.info("SendGiftConsumer 消费者启动成功,nameSrv is {}", rocketMQConsumerProperties.getNameSrv());
    }

    /**
     * 单发
     *
     * @param userId      用户 ID
     * @param bizCode     代码
     * @param msgDataBody msg 数据
     */
    private void sendImMsgSingleton(Long userId, Integer bizCode, JSONObject msgDataBody) {
        ImMsgBody imMsgBody = ImMsgBody.builder()
                .appId(AppIdEnum.LIVE_BIZ_ID.getCode())
                .bizCode(bizCode)
                .userId(userId)
                .data(msgDataBody.toJSONString())
                .build();
        log.info("[SendGiftConsumer] 返回消息 is {}", imMsgBody);
        routerRpc.sendMsg(imMsgBody);
    }

    /**
     * 批量发送礼物
     *
     * @param userIdList  用户 ID 列表
     * @param bizCode     代码
     * @param msgDataBody msg 数据正文
     */
    private void batchSendImMsg(List<Long> userIdList, Integer bizCode, JSONObject msgDataBody) {
        List<ImMsgBody> imMsgBodies = userIdList.stream().map(userId -> ImMsgBody.builder()
                .appId(AppIdEnum.LIVE_BIZ_ID.getCode())
                .bizCode(bizCode)
                .userId(userId)
                .data(msgDataBody.toJSONString())
                .build()).collect(Collectors.toList());
        log.info("[SendGiftConsumer] 返回消息 is {}", imMsgBodies.get(0));
        routerRpc.batchSendMsg(imMsgBodies);
    }

    private void sendPkImMsg(Integer roomId, SendGiftMqDTO sendGiftMqDTO, JSONObject msgDataBody, List<Long> userIdList) {
        String isOverCacheKey = cacheKeyBuilder.buildLivingPkIsOverKey(roomId);
        if (Boolean.TRUE.equals(redisTemplate.hasKey(isOverCacheKey))) {
            return;
        }
        // PK类型设计进度条
        // 假设进度条总长度为1000（500:500）
        /*
         直播间以roomId为维度，给a送礼就是incr，给b送礼就是decr
         开启PK直播间的时候，需要记录两个直播间的信息
        */
        // 获取PK直播间的两个主播ID
        LivingRoomInfoVO respVO = livingRoomRpc.queryLivingRoomByRoomId(roomId);
        Long pkUserId = respVO.getAnchorId();
        Long pkObjectId = livingRoomRpc.queryOnlinePkUserId(roomId);
        if (ObjectUtils.isEmpty(respVO) || ObjectUtils.isEmpty(pkUserId) || ObjectUtils.isEmpty(pkObjectId)) {
            log.error("[SendGiftConsumer] PK直播间信息查询失败，roomId is {}", roomId);
            return;
        }
        long pkNum = 0;
        String pkNumKey = cacheKeyBuilder.buildLivingPkKey(roomId);
        String incrSeqKey = cacheKeyBuilder.buildLivingPkSendSeq(roomId);
        // Long sendGiftSeqNum = redisTemplate.opsForValue().increment(incrSeqKey);
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(LUA_SCRIPT, Long.class);
        Long sendGiftSeqNum = System.currentTimeMillis();
        if (sendGiftMqDTO.getReceiverId().equals(pkUserId)) {
            Integer moveStep = sendGiftMqDTO.getPrice() / 10;
            pkNum = redisTemplate.execute(redisScript, Collections.singletonList(pkNumKey), PK_INIT_NUM, PK_MAX_NUM, PK_MIN_NUM, moveStep);
            if (PK_MAX_NUM <= pkNum) {
                msgDataBody.put("winnerId", pkUserId);
            }
            // resultNum = redisTemplate.opsForValue().increment(pkNumKey, sendGiftMqDTO.getPrice());
            // if (PK_MAX_NUM <= resultNum) {
            //     msgDataBody.put("winnerId", pkUserId);
            //     pkNum = PK_MAX_NUM;
            //     String livingPkIsOverKey = cacheKeyBuilder.buildLivingPkIsOverKey(roomId);
            //     redisTemplate.opsForValue().set(livingPkIsOverKey, true, 5, TimeUnit.MINUTES);
            // } else {
            //     pkNum = resultNum;
            // }
        } else if (sendGiftMqDTO.getReceiverId().equals(pkObjectId)) {
            Integer moveStep = sendGiftMqDTO.getPrice() / 10 * -1;
            pkNum = this.redisTemplate.execute(redisScript,
                    Collections.singletonList(pkNumKey),
                    PK_INIT_NUM,
                    PK_MAX_NUM,
                    PK_MIN_NUM,
                    moveStep);
            if (PK_MIN_NUM >= pkNum) {
                this.redisTemplate.opsForValue().set(cacheKeyBuilder.buildLivingPkIsOverKey(roomId), -1, 5, TimeUnit.MINUTES);
                msgDataBody.put("winnerId", pkObjectId);
            }
            // resultNum = redisTemplate.opsForValue().decrement(pkNumKey, sendGiftMqDTO.getPrice());
            // if (PK_MIN_NUM <= resultNum) {
            //     msgDataBody.put("winnerId", pkObjectId);
            //     String livingPkIsOverKey = cacheKeyBuilder.buildLivingPkIsOverKey(roomId);
            //     redisTemplate.opsForValue().set(livingPkIsOverKey, true, 5, TimeUnit.MINUTES);
            //     pkNum = PK_MIN_NUM;
            // } else {
            //     pkNum = resultNum;
            // }
        }
        msgDataBody.put("receiverId", sendGiftMqDTO.getReceiverId());
        msgDataBody.put("sendGiftSeqNum", sendGiftSeqNum);
        msgDataBody.put("pkNum", pkNum);
        batchSendImMsg(userIdList, ImMsgBizCodeEnum.PK_LIVING_ROOM_IM_GIFT_SUCCESS_MSG.getCode(), msgDataBody);
    }
}
