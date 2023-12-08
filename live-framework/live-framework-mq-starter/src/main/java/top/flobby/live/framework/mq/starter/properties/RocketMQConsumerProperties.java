package top.flobby.live.framework.mq.starter.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 消费者配置
 * @create : 2023-12-08 09:05
 **/

@Data
@Configuration
@ConfigurationProperties(prefix = "live.rmq.consumer")
public class RocketMQConsumerProperties {

    /**
     * 名称空间
     */
    private String nameSrv;
    /**
     * 组名
     */
    private String groupName;

}
