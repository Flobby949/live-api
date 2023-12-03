package top.flobby.live.msg.enums;

import lombok.Getter;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 发送类型枚举
 * @create : 2023-12-03 13:35
 **/

@Getter
public enum SmsSendTypeEnum {

    /**
     * 登录
     */
    LOGIN_OR_REGISTER(1, "登录/注册"),
    ;

    final byte type;
    final String desc;

    SmsSendTypeEnum(int type, String desc) {
        this.type = (byte) type;
        this.desc = desc;
    }
}
