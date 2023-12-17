package top.flobby.live.gift.interfaces;

import top.flobby.live.gift.dto.GiftRecordDTO;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 礼物记录
 * @create : 2023-12-17 13:12
 **/

public interface IGiftRecordRpc {

    /**
     * 添加礼品记录
     *
     * @param giftRecordDTO 礼品记录 DTO
     */
    void addGiftRecord(GiftRecordDTO giftRecordDTO);
}
