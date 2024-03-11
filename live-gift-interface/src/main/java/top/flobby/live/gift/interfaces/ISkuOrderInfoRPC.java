package top.flobby.live.gift.interfaces;

import top.flobby.live.gift.dto.PrepareOrderReqDTO;
import top.flobby.live.gift.dto.SkuOrderInfoDTO;
import top.flobby.live.gift.vo.SkuOrderInfoVO;

/**
 * @author : Flobby
 * @program : live-api
 * @description :
 * @create : 2024-03-11 11:54
 **/

public interface ISkuOrderInfoRPC {

    /**
     * 按用户 ID 和房间 ID 查询最新数据
     *
     * @param userId 用户 ID
     * @param roomId 房间 ID
     * @return {@link SkuOrderInfoVO}
     */
    SkuOrderInfoVO queryLastByUserIdAndRoomId(Long userId, Integer roomId);

    boolean insertOne(SkuOrderInfoDTO skuOrderInfoDTO);

    boolean updateStatus(Integer orderId, Integer status);

    boolean prepareOrder(PrepareOrderReqDTO prepareOrderReqDTO);
}
