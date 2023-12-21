package top.flobby.live.im.router.constants;

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
    LIVING_ROOM_IM_GIFT_SUCCESS_MSG(5556, "直播间送礼成功消息"),
    LIVING_ROOM_IM_GIFT_FAIL_MSG(5557, "直播间送礼失败消息"),
    PK_LIVING_ROOM_IM_GIFT_SUCCESS_MSG(5558, "PK直播间送礼成功"),

    ;

    final Integer code;
    final String desc;

    ImMsgBizCodeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
