package top.flobby.live.gift.provider.consumer;

import com.alibaba.fastjson2.JSON;
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

    @Override
    public void afterPropertiesSet() throws Exception {
        DefaultMQPushConsumer mqPushConsumer = new DefaultMQPushConsumer();
        // 老版本中会开启，新版本的mq不需要使用到
        mqPushConsumer.setVipChannelEnabled(false);
        mqPushConsumer.setNamesrvAddr(rocketMQConsumerProperties.getNameSrv());
        mqPushConsumer.setConsumerGroup(rocketMQConsumerProperties.getGroupName() + "_" + GiftConfigCacheConsumer.class.getSimpleName());
        // 一次从broker中拉取10条消息到本地内存当中进行消费
        mqPushConsumer.setConsumeMessageBatchMaxSize(10);
        mqPushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        // 监听礼物缓存数据更新的行为
        mqPushConsumer.subscribe(GiftProviderTopicNamesConstant.SEND_GIFT, "");
        mqPushConsumer.setMessageListener((MessageListenerConcurrently) (msgs, context) -> {
            for (MessageExt msg : msgs) {
                log.info("[SendGiftConsumer] send gift msg is {}", msg);
                SendGiftMqDTO sendGiftMqDTO = JSON.parseObject(new String(msg.getBody()), SendGiftMqDTO.class);
                AccountTradeDTO accountTradeDTO = AccountTradeDTO.builder()
                        .userId(sendGiftMqDTO.getUserId())
                        .tradeAmount(sendGiftMqDTO.getPrice())
                        .build();
                AccountTradeVO result = currencyAccountRpc.consumeForSendGift(accountTradeDTO);
                // 如果余额扣减成功
                if (result.isOperationSuccess()) {
                    // TODO 触发礼物特效推送
                } else {
                    // TODO 利用IM消息，发送失败消息给用户
                }
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        mqPushConsumer.start();
        log.info("SendGiftConsumer 消费者启动成功,nameSrv is {}", rocketMQConsumerProperties.getNameSrv());
    }
}
