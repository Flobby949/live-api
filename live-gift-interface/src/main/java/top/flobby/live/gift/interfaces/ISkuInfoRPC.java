package top.flobby.live.gift.interfaces;

import top.flobby.live.gift.dto.SkuDetailInfoDTO;
import top.flobby.live.gift.dto.SkuInfoDTO;

import java.util.List;

public interface ISkuInfoRPC {

    /**
     * 根据主播id查询商品信息
     *
     * @param anchorId
     * @return
     */
    List<SkuInfoDTO> queryByAnchorId(Long anchorId);

    /**
     * 查询商品详情
     *
     * @param skuId
     * @return
     */
    SkuDetailInfoDTO queryBySkuId(Long skuId);
}