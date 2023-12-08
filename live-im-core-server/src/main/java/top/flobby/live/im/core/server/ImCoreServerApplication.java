package top.flobby.live.im.core.server;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import top.flobby.live.common.annotation.LiveApplication;

/**
 * @author : Flobby
 * @program : live-api
 * @description : IM 服务启动类
 * @create : 2023-12-07 16:15
 **/

@LiveApplication
@EnableDubbo
public class ImCoreServerApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ImCoreServerApplication.class);
        app.setWebApplicationType(WebApplicationType.NONE);
        app.run(args);
    }
}
