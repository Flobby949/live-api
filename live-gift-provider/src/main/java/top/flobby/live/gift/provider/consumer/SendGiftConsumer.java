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
    public static final Long PK_MAX_NUM = 1000L;
    /**
     * PK 最大数值
     */
    public static final Long PK_MIN_NUM = 0L;

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
                    Long roomId = sendGiftMqDTO.getRoomId();
                    LivingRoomReqDTO livingRoomReqDTO = new LivingRoomReqDTO();
                    livingRoomReqDTO.setId(roomId);
                    livingRoomReqDTO.setAppId(AppIdEnum.LIVE_BIZ_ID.getCode());
                    List<Long> userIdList = livingRoomRpc.queryUserIdByRoomId(livingRoomReqDTO);
                    msgDataBody.put("url", sendGiftMqDTO.getSvgaUrl());
                    // 按类型群发
                    if (SendGiftRoomTypeEnum.PK_SEND_GIFT_ROOM.getCode().equals(sendGiftType)) {
                        // PK类型设计进度条
                        // 假设进度条总长度为1000（500:500）
                        /*
                          直播间以roomId为维度，给a送礼就是incr，给b送礼就是decr
                          开启PK直播间的时候，需要记录两个直播间的信息
                         */
                        String pkNumKey = cacheKeyBuilder.buildLivingPkKey(roomId);
                        // TODO 获取PK直播间的两个主播ID
                        long pkUserId = 1L;
                        long pkObjectId = 2L;
                        Long resultNum;
                        long pkNum = 0;
                        String incrSeqKey = cacheKeyBuilder.buildLivingPkSendSeq(roomId);
                        Long sendGiftSeqNum = redisTemplate.opsForValue().increment(incrSeqKey);
                        if (sendGiftMqDTO.getReceiverId().equals(pkUserId)) {
                            resultNum = redisTemplate.opsForValue().increment(pkNumKey, sendGiftMqDTO.getPrice());
                            if (PK_MAX_NUM <= resultNum) {
                                msgDataBody.put("winnerId", pkUserId);
                                pkNum = PK_MAX_NUM;
                            } else {
                                pkNum = resultNum;
                            }
                        } else if (sendGiftMqDTO.getReceiverId().equals(pkObjectId)) {
                            resultNum = redisTemplate.opsForValue().decrement(pkNumKey, sendGiftMqDTO.getPrice());
                            if (PK_MIN_NUM <= resultNum) {
                                msgDataBody.put("winnerId", pkObjectId);
                                pkNum = PK_MIN_NUM;
                            } else {
                                pkNum = resultNum;
                            }
                        }
                        msgDataBody.put("sendGiftSeqNum", sendGiftSeqNum);
                        msgDataBody.put("pkNum", pkNum);
                        batchSendImMsg(userIdList, ImMsgBizCodeEnum.PK_LIVING_ROOM_IM_GIFT_SUCCESS_MSG.getCode(), msgDataBody);
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
}
