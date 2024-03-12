package top.flobby.live.gift.provider.service;

import top.flobby.live.gift.dto.RollBackStockDTO;
import top.flobby.live.gift.provider.dao.po.SkuStockInfoPO;
import top.flobby.live.gift.provider.service.bo.DcrStockNumBO;

import java.util.List;

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

    /**
     * 批量查询
     *
     * @param skuIds SKU ID
     * @return {@link List}<{@link SkuStockInfoPO}>
     */
    List<SkuStockInfoPO> queryBySkuIds(List<Long> skuIds);

    /**
     * 更新库存
     *
     * @param skuId    SKU ID
     * @param stockNum 库存数量
     * @return boolean
     */
    boolean updateStockNum(Long skuId, Integer stockNum);

    /**
     * 扣减库存，LUA脚本实现方式
     *
     * @param skuId SKU ID
     * @param num   数量
     * @return boolean
     */
    boolean decrStockNumBySkuIdInLua(Long skuId, Integer num);


    /**
     * 批量扣减库存
     *
     * @param skuIds ids
     * @param num    数量
     * @return boolean
     */
    boolean decrStockNumBySkuIdInBatch(List<Long> skuIds, Integer num);

    /**
     * 库存回滚处理机
     *
     * @param dto DTO
     */
    void stockRollBackHandler(RollBackStockDTO dto);
}
