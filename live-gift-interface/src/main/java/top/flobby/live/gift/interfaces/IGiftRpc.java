package top.flobby.live.gift.interfaces;

import top.flobby.live.gift.dto.GiftConfigDTO;

import java.util.List;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 礼物接口
 * @create : 2023-12-17 13:00
 **/

public interface IGiftRpc {

    /**
     * 通过ID获取礼物
     *
     * @param giftId 礼品 ID
     * @return {@link GiftConfigDTO}
     */
    GiftConfigDTO getGiftById(Integer giftId);

    /**
     * 查询礼品清单
     *
     * @return {@link List}<{@link GiftConfigDTO}>
     */
    List<GiftConfigDTO> queryGiftList();

    /**
     * 插入
     *
     * @param giftConfigDTO DTO
     */
    void insertOne(GiftConfigDTO giftConfigDTO);

    /**
     * 更新
     *
     * @param giftConfigDTO DTO
     */
    void updateOne(GiftConfigDTO giftConfigDTO);
}
