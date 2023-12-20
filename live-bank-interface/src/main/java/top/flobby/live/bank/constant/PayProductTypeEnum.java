package top.flobby.live.bank.constant;

import lombok.Getter;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 付费产品类型枚举
 * @create : 2023-12-20 08:32
 **/

@Getter
public enum PayProductTypeEnum {
    LIVING_ROOM_PRODUCT(0, "直播间产品"),
    ;

    final Byte type;
    final String desc;

    PayProductTypeEnum(Integer type, String desc) {
        this.type = type.byteValue();
        this.desc = desc;
    }
}
