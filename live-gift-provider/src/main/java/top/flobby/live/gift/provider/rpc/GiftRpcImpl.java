package top.flobby.live.gift.provider.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import top.flobby.live.gift.dto.GiftDTO;
import top.flobby.live.gift.interfaces.IGiftRpc;
import top.flobby.live.gift.provider.service.IGiftService;

import java.util.List;

/**
 * @author : Flobby
 * @program : live-api
 * @description : impl
 * @create : 2023-12-17 13:00
 **/

@DubboService
public class GiftRpcImpl implements IGiftRpc {

    @Resource
    private IGiftService giftService;

    @Override
    public GiftDTO getGiftById(Integer giftId) {
        return giftService.getGiftById(giftId);
    }

    @Override
    public List<GiftDTO> queryGiftList() {
        return giftService.queryGiftList();
    }

    @Override
    public void insertOne(GiftDTO giftDTO) {
        giftService.insertOne(giftDTO);
    }

    @Override
    public void updateOne(GiftDTO giftDTO) {
        giftService.updateOne(giftDTO);
    }
}
