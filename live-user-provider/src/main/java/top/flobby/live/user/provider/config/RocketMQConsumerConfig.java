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
import top.flobby.live.user.dto.UserDTO;

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
    private RedisTemplate<String, UserDTO> redisTemplate;
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
            defaultMQPushConsumer.subscribe("user-update-cache", "*");
            defaultMQPushConsumer.setMessageListener((MessageListenerConcurrently) (list, consumeConcurrentlyContext) -> {
                UserDTO userDTO = JSON.parseObject(new String(list.get(0).getBody()), UserDTO.class);
                if (userDTO == null || userDTO.getUserId() == null) {
                    log.error("用户id为空, 参数异常：{}", userDTO);
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
                redisTemplate.delete(userProviderCacheKeyBuilder.buildUserInfoKey(userDTO.getUserId()));
                log.info("延迟双删处理, userId is {}", userDTO.getUserId());
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            });
            defaultMQPushConsumer.start();
            log.info("mq消费者启动成功, nameSrv is {}", rocketMQConsumerProperties.getNameSrv());
        } catch (MQClientException e) {
            throw new RuntimeException(e);
        }
    }
}
