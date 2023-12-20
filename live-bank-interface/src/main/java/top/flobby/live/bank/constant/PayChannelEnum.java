package top.flobby.live.bank.constant;

import lombok.Getter;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 支付渠道
 * @create : 2023-12-20 10:33
 **/

@Getter
public enum PayChannelEnum {
    ALIPAY(0, "支付宝"),
    WECHAT(1, "微信"),
    UNION_PAY(2, "银联"),
    CASHIER(3, "收银台"),
    ;

    final Integer code;
    final String desc;

    PayChannelEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static PayChannelEnum find(int code) {
        for (PayChannelEnum value : PayChannelEnum.values()) {
            if (value.code == code) {
                return value;
            }
        }
        return null;
    }
}
