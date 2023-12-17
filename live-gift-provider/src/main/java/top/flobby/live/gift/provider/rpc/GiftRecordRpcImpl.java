package top.flobby.live.gift.provider.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import top.flobby.live.gift.dto.GiftRecordDTO;
import top.flobby.live.gift.interfaces.IGiftRecordRpc;
import top.flobby.live.gift.provider.service.IGiftRecordService;

/**
 * @author : Flobby
 * @program : live-api
 * @description : impl
 * @create : 2023-12-17 13:12
 **/

@DubboService
public class GiftRecordRpcImpl implements IGiftRecordRpc {

    @Resource
    private IGiftRecordService giftRecordService;

    @Override
    public void addGiftRecord(GiftRecordDTO giftRecordDTO) {
        giftRecordService.addGiftRecord(giftRecordDTO);
    }
}
