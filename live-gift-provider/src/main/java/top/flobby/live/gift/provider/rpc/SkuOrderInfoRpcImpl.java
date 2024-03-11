package top.flobby.live.gift.provider.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import top.flobby.live.gift.dto.SkuOrderInfoDTO;
import top.flobby.live.gift.interfaces.ISkuOrderInfoRPC;
import top.flobby.live.gift.provider.service.ISkuOrderInfoService;
import top.flobby.live.gift.vo.SkuOrderInfoVO;

/**
 * @author : Flobby
 * @program : live-api
 * @description :
 * @create : 2024-03-11 11:55
 **/

@DubboService
public class SkuOrderInfoRpcImpl implements ISkuOrderInfoRPC {
    @Resource
    private ISkuOrderInfoService skuOrderInfoService;

    @Override
    public SkuOrderInfoVO queryLastByUserIdAndRoomId(Long userId, Integer roomId) {
        return skuOrderInfoService.queryLastByUserIdAndRoomId(userId, roomId);
    }

    @Override
    public boolean insertOne(SkuOrderInfoDTO skuOrderInfoDTO) {
        return skuOrderInfoService.insertOne(skuOrderInfoDTO);
    }

    @Override
    public boolean updateStatus(Long orderId, Integer status) {
        return skuOrderInfoService.updateStatus(orderId, status);
    }
}
