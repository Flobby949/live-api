package top.flobby.live.user.provider.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 消费者配置信息
 * @create : 2023-11-19 15:22
 **/

@Data
@Configuration
@ConfigurationProperties(prefix = "live.rmq.consumer")
public class RocketMQConsumerProperties {

    // nameServer 地址
    private String nameSrv;
    // 分组名称
    private String groupName;
}
