package top.flobby.live.gift.provider.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import top.flobby.live.gift.dto.GiftConfigDTO;
import top.flobby.live.gift.interfaces.IGiftRpc;
import top.flobby.live.gift.provider.service.IGiftConfigService;

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
    private IGiftConfigService giftService;

    @Override
    public GiftConfigDTO getGiftById(Integer giftId) {
        return giftService.getGiftById(giftId);
    }

    @Override
    public List<GiftConfigDTO> queryGiftList() {
        return giftService.queryGiftList();
    }

    @Override
    public void insertOne(GiftConfigDTO giftConfigDTO) {
        giftService.insertOne(giftConfigDTO);
    }

    @Override
    public void updateOne(GiftConfigDTO giftConfigDTO) {
        giftService.updateOne(giftConfigDTO);
    }
}
