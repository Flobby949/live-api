package top.flobby.live.gift.provider;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.scheduling.annotation.EnableScheduling;
import top.flobby.live.common.annotation.LiveApplication;
import top.flobby.live.gift.interfaces.ISkuStockInfoRPC;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 礼物启动类
 * @create : 2023-12-17 12:38
 **/

@EnableDubbo
@EnableScheduling
@LiveApplication
public class GiftProviderApplication implements CommandLineRunner {

    @Resource
    private ISkuStockInfoRPC skuStockInfoRPC;

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(GiftProviderApplication.class);
        springApplication.setWebApplicationType(WebApplicationType.NONE);
        springApplication.run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        Long anchorId = 1001L;
        Long skuId = 90713L;
        boolean flag = skuStockInfoRPC.prepareStockToRedis(anchorId);
        System.out.println("prepareStockToRedis: " + flag);
        for (int i = 0; i < 11; i++) {
            System.out.println("decrStatus: " + skuStockInfoRPC.decrStockNumBySkuId(skuId, 10));
        }
    }
}
