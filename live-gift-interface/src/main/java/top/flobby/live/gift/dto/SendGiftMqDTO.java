package top.flobby.live.gift.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author : Flobby
 * @program : live-api
 * @description : mq消息DTO
 * @create : 2023-12-18 14:21
 **/

@Data
@Builder
public class SendGiftMqDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -8694426414756404345L;

    private Long userId;
    private Integer giftId;
    private Integer price;
    private Long receiverId;
    private Long roomId;
    private String uuid;
    private String svgaUrl;
    /**
     * 类型
     *
     * @see top.flobby.live.gift.constant.SendGiftRoomTypeEnum
     */
    private Integer type;
}
