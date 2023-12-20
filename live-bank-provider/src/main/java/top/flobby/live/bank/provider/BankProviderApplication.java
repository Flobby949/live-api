package top.flobby.live.bank.provider;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import top.flobby.live.bank.provider.service.IPayProductService;
import top.flobby.live.common.annotation.LiveApplication;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 支付中台
 * @create : 2023-12-17 15:11
 **/

@EnableDubbo
@LiveApplication
public class BankProviderApplication implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(BankProviderApplication.class);
        springApplication.setWebApplicationType(WebApplicationType.NONE);
        springApplication.run(args);
    }

    @Resource
    private IPayProductService payProductService;

    @Override
    public void run(String... args) throws Exception {
        System.out.println(payProductService.productList(0));
    }
}
