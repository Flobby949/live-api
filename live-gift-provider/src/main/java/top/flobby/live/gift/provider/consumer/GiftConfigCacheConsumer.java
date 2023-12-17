package top.flobby.live.gift.provider.consumer;

import com.alibaba.fastjson2.JSON;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import top.flobby.live.common.constants.GiftProviderTopicNamesConstant;
import top.flobby.live.framework.mq.starter.properties.RocketMQConsumerProperties;
import top.flobby.live.framework.redis.starter.key.GiftProviderCacheKeyBuilder;
import top.flobby.live.gift.provider.service.GiftCacheRemoveBO;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 在容器初始化的时候，消费关于礼物cache的主题消息
 * @create : 2023-12-17 16:33
 **/

@Slf4j
@Configuration
public class GiftConfigCacheConsumer implements InitializingBean {

    @Resource
    private RocketMQConsumerProperties rocketMQConsumerProperties;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private GiftProviderCacheKeyBuilder cacheKeyBuilder;

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
        mqPushConsumer.subscribe(GiftProviderTopicNamesConstant.REMOVE_GIFT_CACHE, "");
        mqPushConsumer.setMessageListener((MessageListenerConcurrently) (msgs, context) -> {
            for (MessageExt msg : msgs) {
                GiftCacheRemoveBO giftCacheRemoveBO = JSON.parseObject(new String(msg.getBody()), GiftCacheRemoveBO.class);
                if (giftCacheRemoveBO.isRemoveListCache()) {
                    redisTemplate.delete(cacheKeyBuilder.buildGiftListKey());
                }
                if (giftCacheRemoveBO.getGiftId() > 0) {
                    redisTemplate.delete(cacheKeyBuilder.buildGiftObjKey(giftCacheRemoveBO.getGiftId()));
                }
                log.info("[GiftConfigCacheConsumer] remove gift cache");
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        mqPushConsumer.start();
        log.info("mq消费者启动成功,nameSrv is {}", rocketMQConsumerProperties.getNameSrv());
    }
}
