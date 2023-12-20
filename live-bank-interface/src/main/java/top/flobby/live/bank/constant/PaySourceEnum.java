package top.flobby.live.bank.constant;

import lombok.Getter;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 支付来源枚举
 * @create : 2023-12-20 09:54
 **/

@Getter
public enum PaySourceEnum {
    LIVING_ROOM_PAY(1, "直播间内支付"),
    PERSONAL_CENTER_PAY(2, "个人中心支付"),
    ;

    final Integer code;
    final String desc;

    PaySourceEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static PaySourceEnum find(int code) {
        for (PaySourceEnum value : PaySourceEnum.values()) {
            if (value.code == code) {
                return value;
            }
        }
        return null;
    }
}
