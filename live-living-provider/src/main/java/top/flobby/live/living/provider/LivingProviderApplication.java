package top.flobby.live.living.provider;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import top.flobby.live.common.annotation.LiveApplication;

/**
 * @author : Flobby
 * @program : live-api
 * @description :
 * @create : 2023-12-12 09:37
 **/

@LiveApplication
@EnableDubbo
public class LivingProviderApplication {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(LivingProviderApplication.class);
        springApplication.setWebApplicationType(WebApplicationType.NONE);
        springApplication.run(args);
    }
}
