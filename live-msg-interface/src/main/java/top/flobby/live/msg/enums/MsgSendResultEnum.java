package top.flobby.live.msg.enums;

import lombok.Getter;
import lombok.ToString;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 发送结果枚举
 * @create : 2023-12-03 13:05
 **/

@Getter
@ToString
public enum MsgSendResultEnum {

    SEND_SUCCESS(0, "发送成功"),
    SEND_FAIL(1, "发送失败"),
    MSG_PARAMS_ERROR(2, "参数异常"),
    SEND_TOO_FAST(3, "发送过于频繁"),
    SMS_TIME_OUT(4, "验证码已过期"),
    SMS_CHECK_SUCCESS(5, "验证码校验成功"),
    SMS_CHECK_FAIL(6, "验证码校验失败"),
    ;

    final int code;
    final String desc;

    MsgSendResultEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
