package top.flobby.live.gateway;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import top.flobby.live.common.annotation.LiveApplication;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 启动类
 * @create : 2023-11-30 14:43
 **/

@LiveApplication
@EnableDubbo
public class LiveGatewayApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(LiveGatewayApplication.class);
        app.setWebApplicationType(WebApplicationType.REACTIVE);
        app.run(args);
    }
}
