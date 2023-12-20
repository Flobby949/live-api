package top.flobby.live.bank.constant;

import lombok.Getter;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 订单支付状态枚举
 * @create : 2023-12-20 10:18
 **/

@Getter
public enum OrderStatusEnum {
    WAIT_PAY(0, "待支付"),
    PAYING(1, "支付中"),
    PAY_SUCCESS(2, "已支付"),
    CANCEL(3, "撤销"),
    INVALID(4, "无效");

    final Byte code;
    final String desc;

    OrderStatusEnum(Integer code, String desc) {
        this.code = code.byteValue();
        this.desc = desc;
    }
}
