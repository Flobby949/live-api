package top.flobby.live.user.provider.config;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MQProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 生产者配置类
 * @create : 2023-11-19 15:27
 **/

@Slf4j
@Configuration
public class RocketMQProducerConfig {

    @Resource
    private RocketMQProducerProperties producerProperties;
    @Value("${spring.application.name}")
    private String applicationName;

    @Bean
    public MQProducer mqProducer() {
        DefaultMQProducer defaultMQProducer = new DefaultMQProducer();
        ThreadPoolExecutor asyncThreadPoolExecutor = new ThreadPoolExecutor(4,
                8,
                10,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(10),
                r -> {
                    Thread thread = new Thread(r);
                    thread.setName(applicationName + ":rmq-producer:" + ThreadLocalRandom.current().nextInt(1000));
                    return thread;
                });
        try {
            defaultMQProducer.setNamesrvAddr(producerProperties.getNameSrv());
            defaultMQProducer.setProducerGroup(producerProperties.getGroupName());
            defaultMQProducer.setRetryTimesWhenSendFailed(producerProperties.getRetryTimes());
            defaultMQProducer.setRetryTimesWhenSendAsyncFailed(producerProperties.getRetryTimes());
            defaultMQProducer.setSendMsgTimeout(producerProperties.getSendTimeOut());
            defaultMQProducer.setRetryAnotherBrokerWhenNotStoreOK(true);
            // 设置异步发送线程池
            defaultMQProducer.setAsyncSenderExecutor(asyncThreadPoolExecutor);
            defaultMQProducer.start();
            log.info("mq生产者启动成功, nameSrv is {}", producerProperties.getNameSrv());
        } catch (MQClientException e) {
            throw new RuntimeException(e);
        }
        return defaultMQProducer;
    }
}
