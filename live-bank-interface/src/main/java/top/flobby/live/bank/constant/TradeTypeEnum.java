package top.flobby.live.bank.constant;

import lombok.Getter;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 交易类型枚举
 * @create : 2023-12-18 13:58
 **/

@Getter
public enum TradeTypeEnum {
    SEND_GIFT_TRADE(1, "送礼物交易"),
    ;

    final int code;
    final String desc;

    TradeTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
