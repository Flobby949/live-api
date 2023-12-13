package top.flobby.live.living.enums;

import lombok.Getter;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 直播间类型枚举
 * @create : 2023-12-13 11:01
 **/

@Getter
public enum LivingRoomTypeEnum {

    /**
     * 普通直播间
     */
    NORMAL_ROOM(0, "普通直播间"),
    /**
     * 付费直播间
     */
    PK_ROOM(1, "PK 直播间"),
    ;

    final Integer type;
    final String desc;

    LivingRoomTypeEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }
}
