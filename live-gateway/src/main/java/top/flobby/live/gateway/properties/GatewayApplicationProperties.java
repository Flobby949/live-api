package top.flobby.live.gateway.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 白名单配置类
 * @create : 2023-12-05 15:04
 **/

@Data
@ConfigurationProperties(prefix = "live.gateway")
@Configuration
@RefreshScope
public class GatewayApplicationProperties {

    private List<String> notCheckUrlList;

}
