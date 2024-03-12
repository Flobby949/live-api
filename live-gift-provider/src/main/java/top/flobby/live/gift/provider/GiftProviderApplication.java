package top.flobby.live.gift.provider;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.scheduling.annotation.EnableScheduling;
import top.flobby.live.common.annotation.LiveApplication;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 礼物启动类
 * @create : 2023-12-17 12:38
 **/

@EnableDubbo
@EnableScheduling
@LiveApplication
public class GiftProviderApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(GiftProviderApplication.class);
        springApplication.setWebApplicationType(WebApplicationType.NONE);
        springApplication.run(args);
    }
}
