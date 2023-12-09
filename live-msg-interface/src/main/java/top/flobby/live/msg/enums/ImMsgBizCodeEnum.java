package top.flobby.live.msg.enums;

import lombok.Getter;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 业务消息代码枚举
 * @create : 2023-12-08 13:58
 **/

@Getter
public enum ImMsgBizCodeEnum {

    LIVING_ROOM_IM_CHAT_MSG_BIZ(5555, "直播间IM聊天消息"),
    ;

    final Integer code;
    final String desc;

    ImMsgBizCodeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
