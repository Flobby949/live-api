package top.flobby.live.gift.provider.service;

import top.flobby.live.gift.dto.ShopCarReqDTO;
import top.flobby.live.gift.vo.ShopCarRespVO;

/**
 * @author : Flobby
 * @program : live-api
 * @description :
 * @create : 2024-03-08 13:31
 **/

public interface IShopCarService {
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

    /**
     * 修改购物车中某个商品的数量
     *
     * @param shopCarReqDTO
     */
    Boolean addCarItemNum(ShopCarReqDTO shopCarReqDTO);

    /**
     * 扣减
     *
     * @param shopCarReqDTO dto
     * @return {@link Boolean}
     */
    Boolean decrCarItemNum(ShopCarReqDTO shopCarReqDTO);
}
