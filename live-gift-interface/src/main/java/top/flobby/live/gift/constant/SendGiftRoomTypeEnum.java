package top.flobby.live.gift.constant;

import lombok.Getter;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 送礼类型
 * @create : 2023-12-21 13:16
 **/

@Getter
public enum SendGiftRoomTypeEnum {
    DEFAULT_SEND_GIFT_ROOM(0, "默认送礼直播间"),
    PK_SEND_GIFT_ROOM(1, "PK送礼直播间"),
    ;

    final Integer code;
    final String desc;

    SendGiftRoomTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
