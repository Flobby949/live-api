package top.flobby.live.user.provider.config;

import com.alibaba.fastjson2.JSON;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import top.flobby.live.framework.redis.starter.key.UserProviderCacheKeyBuilder;
import top.flobby.live.user.constants.CacheAsyncDeleteEnum;
import top.flobby.live.user.dto.UserCacheAsyncDeleteDTO;

import static top.flobby.live.user.constants.Constant.CACHE_ASYNC_DELETE;
import static top.flobby.live.user.constants.Constant.USER_ID;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 消费者配置类
 * @create : 2023-11-19 15:27
 **/

@Slf4j
@Configuration
public class RocketMQConsumerConfig implements InitializingBean {

    @Resource
    private RocketMQConsumerProperties rocketMQConsumerProperties;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private UserProviderCacheKeyBuilder userProviderCacheKeyBuilder;

    @Override
    public void afterPropertiesSet() throws Exception {
        initConsumer();
    }

    public void initConsumer() {
        DefaultMQPushConsumer defaultMQPushConsumer = new DefaultMQPushConsumer();

        try {
            defaultMQPushConsumer.setNamesrvAddr(rocketMQConsumerProperties.getNameSrv());
            defaultMQPushConsumer.setConsumerGroup(rocketMQConsumerProperties.getGroupName());
            defaultMQPushConsumer.setConsumeMessageBatchMaxSize(1);
            defaultMQPushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
            // 设置监听的主题和标签
            defaultMQPushConsumer.subscribe(CACHE_ASYNC_DELETE, "*");
            // 设置消息监听器
            defaultMQPushConsumer.setMessageListener((MessageListenerConcurrently) (list, consumeConcurrentlyContext) -> {
                UserCacheAsyncDeleteDTO cacheAsyncDeleteDTO =
                        JSON.parseObject(new String(list.get(0).getBody()), UserCacheAsyncDeleteDTO.class);
                if (cacheAsyncDeleteDTO == null || cacheAsyncDeleteDTO.getCode() == null) {
                    log.error("code 为空, 参数异常：{}", cacheAsyncDeleteDTO);
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
                // 区分不同的删除操作
                Integer deleteCode = cacheAsyncDeleteDTO.getCode();
                Long userId = JSON.parseObject(cacheAsyncDeleteDTO.getJson()).getLong(USER_ID);
                if (CacheAsyncDeleteEnum.USER_INFO_DELETE.getCode().equals(deleteCode)) {
                    // 删除用户信息
                    redisTemplate.delete(userProviderCacheKeyBuilder.buildUserInfoKey(userId));
                } else if (CacheAsyncDeleteEnum.USER_TAG_DELETE.getCode().equals(deleteCode)) {
                    // 删除用户标签
                    redisTemplate.delete(userProviderCacheKeyBuilder.buildUserTagKey(userId));
                }
                log.info("{} 延迟双删处理, userId is {}", CacheAsyncDeleteEnum.getDescByCode(deleteCode), userId);
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            });
            defaultMQPushConsumer.start();
            log.info("mq消费者启动成功, nameSrv is {}", rocketMQConsumerProperties.getNameSrv());
        } catch (MQClientException e) {
            throw new RuntimeException(e);
        }
    }
}
