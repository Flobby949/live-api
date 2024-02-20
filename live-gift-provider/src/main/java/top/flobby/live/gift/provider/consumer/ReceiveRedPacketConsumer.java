package top.flobby.live.gift.provider.consumer;

import com.alibaba.fastjson2.JSON;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;
import top.flobby.live.common.constants.GiftProviderTopicNamesConstant;
import top.flobby.live.framework.mq.starter.properties.RocketMQConsumerProperties;
import top.flobby.live.gift.provider.service.IRedPacketService;
import top.flobby.live.gift.provider.service.bo.SendRedPacketBO;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 领取红包消费者
 * @create : 2024-01-12 14:34
 **/

@Slf4j
@Configuration
public class ReceiveRedPacketConsumer implements InitializingBean {

    @Resource
    private RocketMQConsumerProperties rocketMQConsumerProperties;
    @Resource
    private IRedPacketService redPacketService;

    @Override
    public void afterPropertiesSet() throws Exception {
        DefaultMQPushConsumer mqPushConsumer = new DefaultMQPushConsumer();
        mqPushConsumer.setVipChannelEnabled(false);
        mqPushConsumer.setNamesrvAddr(rocketMQConsumerProperties.getNameSrv());
        mqPushConsumer.setConsumerGroup(rocketMQConsumerProperties.getGroupName() + "_" + ReceiveRedPacketConsumer.class.getSimpleName());
        mqPushConsumer.setConsumeMessageBatchMaxSize(1);
        mqPushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        // 监听领取红包的行为
        mqPushConsumer.subscribe(GiftProviderTopicNamesConstant.RECEIVE_RED_PACKET, "");
        mqPushConsumer.setMessageListener((MessageListenerConcurrently) (msgs, context) -> {
            try {
                SendRedPacketBO redPacket = JSON.parseObject(msgs.get(0).getBody(), SendRedPacketBO.class);
                log.info("[ReceiveRedPacketConsumer] receive red packet: {}", redPacket);
                redPacketService.receiveRedPacketHandle(redPacket.getReqDTO(), redPacket.getPrice());
            } catch (Exception e) {
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        mqPushConsumer.start();
        log.info("GiftConfigCacheConsumer 消费者启动成功,nameSrv is {}", rocketMQConsumerProperties.getNameSrv());
    }
}
