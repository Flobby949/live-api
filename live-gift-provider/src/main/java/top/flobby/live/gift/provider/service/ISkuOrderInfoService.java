package top.flobby.live.gift.provider.service;

import top.flobby.live.gift.dto.SkuOrderInfoDTO;
import top.flobby.live.gift.vo.SkuOrderInfoVO;

/**
 * @author : Flobby
 * @program : live-api
 * @description :
 * @create : 2024-03-11 11:56
 **/

public interface ISkuOrderInfoService {

    /**
     * 按用户 ID 和房间 ID 查询最新数据
     *
     * @param userId 用户 ID
     * @param roomId 房间 ID
     * @return {@link SkuOrderInfoVO}
     */
    SkuOrderInfoVO queryLastByUserIdAndRoomId(Long userId, Integer roomId);

    boolean insertOne(SkuOrderInfoDTO skuOrderInfoDTO);

    boolean updateStatus(Long orderId, Integer status);
}
