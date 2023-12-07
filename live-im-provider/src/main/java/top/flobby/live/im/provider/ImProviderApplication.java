package top.flobby.live.im.provider;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import top.flobby.live.common.annotation.LiveApplication;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 启动类
 * @create : 2023-12-07 16:52
 **/

@LiveApplication
@EnableDubbo
public class ImProviderApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ImProviderApplication.class);
        app.setWebApplicationType(WebApplicationType.NONE);
        app.run(args);
    }
}
