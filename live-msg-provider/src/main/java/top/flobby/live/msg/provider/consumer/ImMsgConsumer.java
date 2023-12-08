package top.flobby.live.msg.provider.consumer;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import top.flobby.live.common.constants.ImCoreServerTopicNameConstant;
import top.flobby.live.framework.mq.starter.properties.RocketMQConsumerProperties;

/**
 * @author : Flobby
 * @program : live-api
 * @description : IM业务消息消费者
 * @create : 2023-12-08 09:22
 **/

@Slf4j
@Component
public class ImMsgConsumer implements InitializingBean {

    @Resource
    private RocketMQConsumerProperties rocketMQConsumerProperties;

    @Override
    public void afterPropertiesSet() throws Exception {
        DefaultMQPushConsumer mqPushConsumer = new DefaultMQPushConsumer();
        mqPushConsumer.setVipChannelEnabled(false);
        mqPushConsumer.setNamesrvAddr(rocketMQConsumerProperties.getNameSrv());
        mqPushConsumer.setConsumerGroup(rocketMQConsumerProperties.getGroupName() + "_" + ImMsgConsumer.class.getSimpleName());
        mqPushConsumer.setConsumeMessageBatchMaxSize(10);
        mqPushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        // 监听im发送过来的业务消息topic
        mqPushConsumer.subscribe(ImCoreServerTopicNameConstant.LIVE_IM_BIZ_MSG_TOPIC, "");
        mqPushConsumer.setMessageListener((MessageListenerConcurrently) (msgs, context) -> {
            String json = new String(msgs.get(0).getBody());
            log.info("接收到im业务消息:{}", json);
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        mqPushConsumer.start();
        log.info("mq消费者启动成功,nameSrv is {}", rocketMQConsumerProperties.getNameSrv());
    }
}
