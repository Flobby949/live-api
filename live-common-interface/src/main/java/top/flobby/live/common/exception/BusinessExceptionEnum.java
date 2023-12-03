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
    CODE_ERROR("验证码错误"),
    ;

    private final String desc;

    BusinessExceptionEnum(String desc) {
        this.desc = desc;
    }
}