package top.flobby.live.gift.interfaces;

import top.flobby.live.gift.dto.ShopCarReqDTO;
import top.flobby.live.gift.vo.ShopCarRespVO;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 商品购物车rpc
 * @create : 2024-03-08 13:29
 **/

public interface IShopCarRPC {

    /**
     * 查看购物车信息
     *
     * @param shopCarReqDTO
     */
    ShopCarRespVO getCarInfo(ShopCarReqDTO shopCarReqDTO);

    /**
     * 添加商品到购物车中
     *
     * @param shopCarReqDTO
     */
    Boolean addCar(ShopCarReqDTO shopCarReqDTO);


    /**
     * 移除购物车
     *
     * @param shopCarReqDTO
     */
    Boolean removeFromCar(ShopCarReqDTO shopCarReqDTO);

    /**
     * 清除整个购物车
     *
     * @param shopCarReqDTO
     */
    Boolean clearShopCar(ShopCarReqDTO shopCarReqDTO);
}
