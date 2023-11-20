package top.flobby.live.id.generate;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 启动类
 * @create : 2023-11-20 11:04
 **/

@SpringBootApplication(scanBasePackages = {"top.flobby.live"})
@EnableDiscoveryClient
@EnableDubbo
public class IdGenerateProvider {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(IdGenerateProvider.class);
        springApplication.setWebApplicationType(WebApplicationType.NONE);
        springApplication.run(args);
    }
}
