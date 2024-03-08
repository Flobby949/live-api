package top.flobby.live.gift.interfaces;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 库存rpc
 * @create : 2024-03-08 13:57
 **/

public interface ISkuStockInfoRPC {

    /**
     * 根据skuId更新库存值
     *
     * @param skuId SKU ID
     * @param num   数量
     * @return boolean
     */
    boolean dcrStockNumBySkuId(Long skuId, Integer num);
}
