package top.flobby.live.framework.mq.starter.producer;

import jakarta.annotation.Resource;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MQProducer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.flobby.live.framework.mq.starter.properties.RocketMQProducerProperties;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 生产者配置
 * @create : 2023-12-08 09:06
 **/


@Configuration
public class RocketMQProducerConfig {

    @Resource
    private RocketMQProducerProperties rocketMQProducerProperties;

    @Bean
    public MQProducer mqProducer() {
        ThreadPoolExecutor asyncThreadPool = new ThreadPoolExecutor(
                Runtime.getRuntime().availableProcessors() * 2,
                20,
                30,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(500), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "rocketmq-async-thread-" + new Random().ints().toString());
            }
        });
        DefaultMQProducer defaultMqProducer = getDefaultMqProducer(asyncThreadPool);
        try {
            defaultMqProducer.start();
            System.out.println("=============== mq生产者启动成功,nameSrv is " + rocketMQProducerProperties.getNameSrv() + " ==================");
        } catch (MQClientException e) {
            throw new RuntimeException(e);
        }
        return defaultMqProducer;
    }

    private DefaultMQProducer getDefaultMqProducer(ThreadPoolExecutor asyncThreadPool) {
        DefaultMQProducer defaultMqProducer = new DefaultMQProducer();
        defaultMqProducer.setProducerGroup(rocketMQProducerProperties.getGroupName());
        defaultMqProducer.setNamesrvAddr(rocketMQProducerProperties.getNameSrv());
        defaultMqProducer.setRetryTimesWhenSendFailed(rocketMQProducerProperties.getRetryTimes());
        defaultMqProducer.setRetryTimesWhenSendAsyncFailed(rocketMQProducerProperties.getRetryTimes());
        defaultMqProducer.setRetryAnotherBrokerWhenNotStoreOK(true);
        defaultMqProducer.setAsyncSenderExecutor(asyncThreadPool);
        return defaultMqProducer;
    }
}
