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

    /**
     * 库存值从mysql预热加载到redis
     *
     * @param anchorId 主播id
     * @return boolean
     */
    boolean prepareStockToRedis(Long anchorId);

    /**
     * 按 SKU ID 查询库存
     * 基础的缓存查询接口
     *
     * @param skuId SKU ID
     * @return {@link Integer}
     */
    Integer queryStockNumBySkuId(Long skuId);

    /**
     * 将 STOCK 同步到 MySQL
     *
     * @param anchorId 主播id
     * @return boolean
     */
    boolean syncStockToMysql(Long anchorId);


    // 库存扣减设计lua脚本


    // 库存扣减成功后，生成待支付订单MQ
}
