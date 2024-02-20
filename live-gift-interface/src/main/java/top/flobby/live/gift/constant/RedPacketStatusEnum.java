package top.flobby.live.gift.constant;

import lombok.Getter;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 红包雨状态枚举
 * @create : 2024-01-07 21:50
 **/

@Getter
public enum RedPacketStatusEnum {

    /**
     * 待准备
     */
    WAITING(1, "待准备"),
    /**
     * 已准备
     */
    PREPARED(2, "已准备"),
    /**
     * 已发送
     */
    HAS_SENT(3, "已发送"),
    ;

    final Byte code;
    final String desc;

    RedPacketStatusEnum(Integer code, String desc) {
        this.code = code.byteValue();
        this.desc = desc;
    }
}
