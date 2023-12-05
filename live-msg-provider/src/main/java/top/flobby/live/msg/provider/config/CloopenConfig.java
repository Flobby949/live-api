package top.flobby.live.msg.provider.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 容联云配置
 * @create : 2023-12-05 12:13
 **/

@Data
@RefreshScope
@Configuration
@ConfigurationProperties(prefix = "live.sms.ccp")
public class CloopenConfig {

    private String serverIp;
    private String port;
    private String accountSId;
    private String accountToken;
    private String appId;
    private String templateId;
}
