package top.flobby.live.gift.provider.service;

import top.flobby.live.gift.provider.dao.po.SkuStockInfoPO;
import top.flobby.live.gift.provider.service.bo.DcrStockNumBO;

/**
 * @author : Flobby
 * @program : live-api
 * @description :
 * @create : 2024-03-08 14:02
 **/

public interface ISkuStockInfoService {

    /**
     * 按SKU ID减少库存
     *
     * @param skuId SKU ID
     * @param num   数量
     * @return {@link DcrStockNumBO}
     */
    DcrStockNumBO dcrStockNumBySkuId(Long skuId, Integer num);

    /**
     * 按 SKU ID 查询库存信息
     *
     * @param skuId SKU ID
     * @return {@link SkuStockInfoPO}
     */
    SkuStockInfoPO queryBySkuId(Long skuId);
}
