package top.flobby.live.im.common;

import lombok.Getter;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 消息包类型枚举
 * @create : 2023-12-07 15:30
 **/

@Getter
public enum ImMsgCodeEnum {

    IM_LOGIN_MSG(1001, "登录消息包"),
    IM_LOGOUT_MSG(1002, "登出消息包"),
    IM_BUSINESS_MSG(1003, "常规业务消息包"),
    IM_HEARTBEAT_MSG(1004, "心跳消息包"),
    IM_ACK_MSG(1005, "ACK消息包"),
    ;

    final int code;
    final String desc;

    ImMsgCodeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
