package top.flobby.live.gift.provider.service;

import top.flobby.live.gift.provider.dao.po.SkuInfoPO;

import java.util.List;

/**
 * SKU信息服务
 *
 * @author Flobby
 * @date 2024/02/26
 */
public interface ISkuInfoService {

    /**
     * 批量skuId查询
     *
     * @param skuIdList SKU ID 列表
     * @return {@link List}<{@link SkuInfoPO}>
     */
    List<SkuInfoPO> queryBySkuIds(List<Long> skuIdList);

    /**
     * 查询商品详情
     *
     * @param skuId SKU ID
     * @return {@link SkuInfoPO}
     */
    SkuInfoPO queryBySkuId(Long skuId);

    /**
     * 查询商品详情
     *
     * @param skuId SKU ID
     * @return {@link SkuInfoPO}
     */
    SkuInfoPO queryBySkuIdFromCache(Long skuId);
}