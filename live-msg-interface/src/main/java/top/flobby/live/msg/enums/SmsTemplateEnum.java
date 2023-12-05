package top.flobby.live.msg.enums;

import lombok.Getter;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 短信模板枚举
 * @create : 2023-12-05 12:27
 **/

@Getter
public enum SmsTemplateEnum {

    /**
     * 登录验证码
     */
    LOGIN_CODE_TEMPLATE("1", "登录验证码"),
    ;

    final String templateId;
    final String desc;

    SmsTemplateEnum(String templateId, String desc) {
        this.templateId = templateId;
        this.desc = desc;
    }
}
