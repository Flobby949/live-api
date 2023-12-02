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
    MEMBER_PHONE_EXIST("手机号已存在"),
    MEMBER_PHONE_NOT_EXIST("手机号不存在"),
    MEMBER_CODE_ERROR("验证码错误"),
    ;

    private final String desc;

    BusinessExceptionEnum(String desc) {
        this.desc = desc;
    }
}