package top.flobby.live.account;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import top.flobby.live.common.annotation.LiveApplication;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 启动类
 * @create : 2023-12-05 12:58
 **/

@LiveApplication
@EnableDubbo
public class AccountApplication implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(AccountApplication.class);
        springApplication.setWebApplicationType(WebApplicationType.NONE);
        springApplication.run(args);

    }

    @Override
    public void run(String... args) throws Exception {
        // String token = accountService.createAndSaveLoginToken(1L);
        // System.out.println(token);
        // Long userId = accountService.getUserIdByToken(token);
        // System.out.println(userId);
    }
}
