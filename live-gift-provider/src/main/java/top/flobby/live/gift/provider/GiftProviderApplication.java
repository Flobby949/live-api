package top.flobby.live.gift.provider;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.scheduling.annotation.EnableScheduling;
import top.flobby.live.common.annotation.LiveApplication;
import top.flobby.live.gift.dto.RollBackStockDTO;
import top.flobby.live.gift.provider.service.ISkuStockInfoService;

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

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(GiftProviderApplication.class);
        springApplication.setWebApplicationType(WebApplicationType.NONE);
        springApplication.run(args);
    }

    @Resource
    private ISkuStockInfoService skuStockInfoService;

    @Override
    public void run(String... args) throws Exception {
        RollBackStockDTO rollBackStockDTO = new RollBackStockDTO();
        rollBackStockDTO.setOrderId(29);
        rollBackStockDTO.setUserId(11061L);
        skuStockInfoService.stockRollBackHandler(rollBackStockDTO);

    }
}
