package top.flobby.live.im.core.server.consumer;

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
import top.flobby.live.common.constants.ImCoreServerTopicNameConstant;
import top.flobby.live.framework.mq.starter.properties.RocketMQConsumerProperties;
import top.flobby.live.im.core.server.service.IMsgAckCheckService;
import top.flobby.live.im.core.server.service.IRouterHandlerService;
import top.flobby.live.im.dto.ImMsgBody;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 消费者配置类
 * @create : 2023-11-19 15:27
 **/

@Slf4j
@Configuration
public class ImAckConsumer implements InitializingBean {


    @Resource
    private RocketMQConsumerProperties rocketMQConsumerProperties;
    @Resource
    private IMsgAckCheckService msgAckCheckService;
    @Resource
    private IRouterHandlerService routerHandlerService;

    @Override
    public void afterPropertiesSet() throws Exception {
        initConsumer();
    }

    public void initConsumer() {
        DefaultMQPushConsumer defaultMQPushConsumer = new DefaultMQPushConsumer();

        try {
            defaultMQPushConsumer.setNamesrvAddr(rocketMQConsumerProperties.getNameSrv());
            defaultMQPushConsumer.setConsumerGroup(rocketMQConsumerProperties.getGroupName() + "_" + ImAckConsumer.class.getSimpleName());
            // 每次拉取一条消息
            defaultMQPushConsumer.setConsumeMessageBatchMaxSize(1);
            defaultMQPushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
            // 设置监听的主题和标签
            defaultMQPushConsumer.subscribe(ImCoreServerTopicNameConstant.LIVE_IM_ACK_MSG_TOPIC, "*");
            // 设置消息监听器
            defaultMQPushConsumer.setMessageListener((MessageListenerConcurrently) (list, consumeConcurrentlyContext) -> {
                String json = new String(list.get(0).getBody());
                ImMsgBody imMsgBody = JSON.parseObject(json, ImMsgBody.class);
                int retryTime = msgAckCheckService.getAckMsgTimes(imMsgBody);
                log.info("收到消息确认请求, msg: {}, retryTime: {}", json, retryTime);
                if (retryTime < 0) {
                    // 已经被移除，ACK确认成功
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
                if (retryTime < 2) {
                    // 只重试一次
                    msgAckCheckService.recordAckMsg(imMsgBody, retryTime + 1);
                    msgAckCheckService.sendDelayMsg(imMsgBody);
                    routerHandlerService.sendMsgToClient(imMsgBody);
                } else {
                    msgAckCheckService.doMsgCheck(imMsgBody);
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            });
            defaultMQPushConsumer.start();
            log.info("mq消费者启动成功, nameSrv is {}", rocketMQConsumerProperties.getNameSrv());
        } catch (MQClientException e) {
            throw new RuntimeException(e);
        }
    }
}
