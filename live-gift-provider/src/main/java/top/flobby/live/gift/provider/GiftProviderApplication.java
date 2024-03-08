package top.flobby.live.gift.provider;

import com.alibaba.fastjson2.JSON;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import top.flobby.live.common.annotation.LiveApplication;
import top.flobby.live.gift.dto.ShopCarReqDTO;
import top.flobby.live.gift.provider.service.IShopCarService;
import top.flobby.live.gift.vo.ShopCarRespVO;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 礼物启动类
 * @create : 2023-12-17 12:38
 **/

@EnableDubbo
@LiveApplication
public class GiftProviderApplication implements CommandLineRunner {

    @Resource
    private IShopCarService shopCarService;

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(GiftProviderApplication.class);
        springApplication.setWebApplicationType(WebApplicationType.NONE);
        springApplication.run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        ShopCarReqDTO carReqDTO = new ShopCarReqDTO();
        carReqDTO.setUserId(1001L);
        carReqDTO.setRoomId(1);
        carReqDTO.setSkuId(90713L);
        shopCarService.addCar(carReqDTO);
        shopCarService.addCarItemNum(carReqDTO);
        ShopCarRespVO respDTO = shopCarService.getCarInfo(carReqDTO);
        System.out.println("购物车信息：" + JSON.toJSONString(respDTO));
        // shopCarService.removeFromCar(carReqDTO);
        // respDTO = shopCarService.getCarInfo(carReqDTO);
        // System.out.println("购物车信息：" + JSON.toJSONString(respDTO));
        // shopCarService.clearShopCar(carReqDTO);
        // respDTO = shopCarService.getCarInfo(carReqDTO);
        // System.out.println("购物车信息：" + JSON.toJSONString(respDTO));
    }
}
