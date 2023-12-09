package top.flobby.live.im.router.provider;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import top.flobby.live.common.annotation.LiveApplication;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 启动类
 * @create : 2023-12-08 10:18
 **/

@LiveApplication
@EnableDubbo
public class ImRouterApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(ImRouterApplication.class);
        springApplication.setWebApplicationType(WebApplicationType.NONE);
        springApplication.run(args);
    }
}
