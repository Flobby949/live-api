package top.flobby.live.gift.provider.service;

import java.util.List;

/**
 * iAnchor 店铺资讯服务
 *
 * @author Flobby
 * @date 2024/02/26
 */
public interface IAnchorShopInfoService {

    /**
     * 根据主播id查询skuId信息
     *
     * @param anchorId 主播 ID
     * @return {@link List}<{@link Long}>
     */
    List<Long> querySkuIdByAnchorId(Long anchorId);

    /**
     * 查询所有有效主播ID
     *
     * @return {@link List}<{@link Long}>
     */
    List<Long> queryAllValidAnchorId();
}