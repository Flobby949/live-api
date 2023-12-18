package top.flobby.live.api.dto;

import lombok.Data;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 送礼dto
 * @create : 2023-12-17 16:51
 **/

@Data
public class GiftDTO {
    private int giftId;
    private Long roomId;
    private Long senderUserId;
    private Long receiverId;
}
