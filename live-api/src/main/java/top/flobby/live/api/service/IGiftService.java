package top.flobby.live.api.service;

import top.flobby.live.api.dto.GiftDTO;
import top.flobby.live.api.vo.GiftConfigVO;

import java.util.List;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 礼物服务
 * @create : 2023-12-17 16:52
 **/

public interface IGiftService {

    /**
     * 展示礼物列表
     *
     * @return {@link List}<{@link GiftConfigVO}>
     */
    List<GiftConfigVO> listGift();

    /**
     * 送礼
     *
     * @param giftDTO DTO
     */
    void sendGift(GiftDTO giftDTO);
}
