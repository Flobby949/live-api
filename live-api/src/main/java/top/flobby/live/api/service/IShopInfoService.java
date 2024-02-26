package top.flobby.live.api.service;

import top.flobby.live.api.dto.SkuInfoReqDTO;
import top.flobby.live.api.vo.SkuDetailInfoVO;
import top.flobby.live.api.vo.SkuInfoVO;

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
}
