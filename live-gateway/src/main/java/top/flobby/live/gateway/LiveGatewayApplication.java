package top.flobby.live.gateway;

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
public class LiveGatewayApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(LiveGatewayApplication.class);
        app.setWebApplicationType(WebApplicationType.REACTIVE);
        app.run(args);
    }
}
