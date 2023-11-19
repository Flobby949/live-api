package top.flobby.live.user.provider.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 生产者配置信息
 * @create : 2023-11-19 15:22
 **/

@Data
@Configuration
@ConfigurationProperties(prefix = "live.rmq.producer")
public class RocketMQProducerProperties {

    // nameServer 地址
    private String nameSrv;
    // 分组名称
    private String groupName;
    // 重试次数
    private int retryTimes;
    // 超时时间
    private int sendTimeOut;
}
