package top.flobby.live.gift.provider.service;

import top.flobby.live.gift.dto.GiftDTO;

import java.util.List;

/**
 * @author : Flobby
 * @program : live-api
 * @description :
 * @create : 2023-12-17 13:00
 **/

public interface IGiftService {
    /**
     * 通过ID获取礼物
     *
     * @param giftId 礼品 ID
     * @return {@link GiftDTO}
     */
    GiftDTO getGiftById(Integer giftId);

    /**
     * 查询礼品清单
     *
     * @return {@link List}<{@link GiftDTO}>
     */
    List<GiftDTO> queryGiftList();

    /**
     * 插入
     *
     * @param giftDTO DTO
     */
    void insertOne(GiftDTO giftDTO);

    /**
     * 更新
     *
     * @param giftDTO DTO
     */
    void updateOne(GiftDTO giftDTO);
}
