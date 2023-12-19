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
import top.flobby.live.gift.dto.SendGiftMqDTO;
import top.flobby.live.im.common.AppIdEnum;
import top.flobby.live.im.dto.ImMsgBody;
import top.flobby.live.im.router.constants.ImMsgBizCodeEnum;
import top.flobby.live.im.router.interfaces.ImRouterRpc;

import java.util.concurrent.TimeUnit;

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
                boolean lockStatus = redisTemplate.opsForValue().setIfAbsent(mqConsumeKey, -1, 5, TimeUnit.MINUTES);
                if (!lockStatus) {
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
                ImMsgBody imMsgBody = ImMsgBody.builder()
                        .appId(AppIdEnum.LIVE_BIZ_ID.getCode())
                        .build();
                JSONObject msgDataBody = new JSONObject();
                // todo 当前特效为接收人可见，后续需要改为全房间可见
                if (result.isOperationSuccess()) {
                    // 触发礼物特效推送
                    msgDataBody.put("url", sendGiftMqDTO.getSvgaUrl());
                    // 成功消息code
                    imMsgBody.setBizCode(ImMsgBizCodeEnum.LIVING_ROOM_IM_GIFT_SUCCESS_MSG.getCode());
                    // 接收方为收礼人
                    imMsgBody.setUserId(sendGiftMqDTO.getReceiverId());
                } else {
                    // 利用IM消息，发送失败消息给用户
                    msgDataBody.put("msg", result.getMessage());
                    // 失败消息code
                    imMsgBody.setBizCode(ImMsgBizCodeEnum.LIVING_ROOM_IM_GIFT_FAIL_MSG.getCode());
                    // 接收方为送礼人,通知失败消息
                    imMsgBody.setUserId(sendGiftMqDTO.getUserId());
                }
                imMsgBody.setData(msgDataBody.toJSONString());
                log.info("[SendGiftConsumer] 返回消息 is {}", imMsgBody);
                routerRpc.sendMsg(imMsgBody);
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        mqPushConsumer.start();
        log.info("SendGiftConsumer 消费者启动成功,nameSrv is {}", rocketMQConsumerProperties.getNameSrv());
    }
}
