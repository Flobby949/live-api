package top.flobby.live.common.exception;

import lombok.Getter;
import lombok.ToString;

/**
 * @author Flobby
 */

@Getter
@ToString
public enum BusinessExceptionEnum {

    OTHER_ERROR("未知异常！"),
    PARAMS_ERROR("参数错误！"),
    USER_PHONE_EXIST("手机号已存在"),
    USER_PHONE_NOT_EXIST("手机号不存在"),
    CODE_SEND_FAIL("验证码发送失败"),
    CODE_ERROR("验证码错误"),
    ID_GENERATE_ERROR("ID生成异常"),
    TOKEN_TIMEOUT("token过期"),
    TOKEN_ERROR("token异常"),
    LIVING_ROOM_IS_NOT_EXIST("直播间不存在"),
    ;

    private final String desc;

    BusinessExceptionEnum(String desc) {
        this.desc = desc;
    }
}