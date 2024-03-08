package top.flobby.live.api.service;

import top.flobby.live.api.dto.ShopCarReq;
import top.flobby.live.api.dto.SkuInfoReqDTO;
import top.flobby.live.api.vo.SkuDetailInfoVO;
import top.flobby.live.api.vo.SkuInfoVO;
import top.flobby.live.gift.vo.ShopCarRespVO;

import java.util.List;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 带货相关
 * @create : 2024-02-26 13:31
 **/

public interface IShopInfoService {

    /**
     * 根据直播间id查询商品信息
     *
     * @param roomId 房间 ID
     * @return {@link List}<{@link SkuInfoVO}>
     */
    List<SkuInfoVO> queryByRoomId(Integer roomId);

    /**
     * 查询商品详情
     *
     * @param skuInfoReqDTO SKU info req DTO
     * @return {@link SkuDetailInfoVO}
     */
    SkuDetailInfoVO detail(SkuInfoReqDTO skuInfoReqDTO);

    /**
     * 添加商品到购物车
     *
     * @param shopCarReq
     */
    Boolean addCar(ShopCarReq shopCarReq);


    /**
     * 查看购物车信息
     *
     * @param shopCarReq
     */
    ShopCarRespVO getCarInfo(ShopCarReq shopCarReq);

    /**
     * 移除购物车
     *
     * @param shopCarReq
     */
    Boolean removeFromCar(ShopCarReq shopCarReq);

    /**
     * 清除整个购物车
     *
     * @param shopCarReq
     */
    Boolean clearShopCar(ShopCarReq shopCarReq);
}
