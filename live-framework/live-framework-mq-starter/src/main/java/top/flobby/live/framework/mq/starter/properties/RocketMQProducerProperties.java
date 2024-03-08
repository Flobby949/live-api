package top.flobby.live.framework.mq.starter.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 生产者配置
 * @create : 2023-12-08 09:05
 **/

@Data
@Configuration
@ConfigurationProperties(prefix = "live.rmq.producer")
public class RocketMQProducerProperties {

    /**
     * 命名空间
     */
    private String nameSrv;
    /**
     * 组名
     */
    private String groupName;
    /**
     * 应用程序名称
     */
    private String applicationName;
    /**
     * 发送消息超时
     */
    private Integer sendMsgTimeout;
    /**
     * 重试次数
     */
    private Integer retryTimes;
    /**
     * 访问密钥
     */
    private String accessKey;
    /**
     * 密钥
     */
    private String secretKey;
}
