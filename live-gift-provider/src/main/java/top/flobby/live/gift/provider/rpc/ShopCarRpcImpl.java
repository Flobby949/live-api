package top.flobby.live.gift.provider.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import top.flobby.live.gift.dto.ShopCarReqDTO;
import top.flobby.live.gift.interfaces.IShopCarRPC;
import top.flobby.live.gift.provider.service.IShopCarService;
import top.flobby.live.gift.vo.ShopCarRespVO;

/**
 * @author : Flobby
 * @program : live-api
 * @description :
 * @create : 2024-03-08 13:30
 **/

@DubboService
public class ShopCarRpcImpl implements IShopCarRPC {
    @Resource
    private IShopCarService shopCarService;

    @Override
    public ShopCarRespVO getCarInfo(ShopCarReqDTO shopCarReqDTO) {
        return shopCarService.getCarInfo(shopCarReqDTO);
    }

    @Override
    public Boolean addCar(ShopCarReqDTO shopCarReqDTO) {
        return shopCarService.addCar(shopCarReqDTO);
    }

    @Override
    public Boolean removeFromCar(ShopCarReqDTO shopCarReqDTO) {
        return shopCarService.removeFromCar(shopCarReqDTO);
    }

    @Override
    public Boolean clearShopCar(ShopCarReqDTO shopCarReqDTO) {
        return shopCarService.clearShopCar(shopCarReqDTO);
    }
}
