package top.flobby.live.common.enums;

import lombok.Getter;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 网关请求头枚举
 * @create : 2023-12-05 15:32
 **/

@Getter
public enum GatewayHeaderEnum {

    USER_LOGIN_ID("live-user-id", "用户登录ID"),
    ;

    final String header;
    final String desc;

    GatewayHeaderEnum(String header, String desc) {
        this.header = header;
        this.desc = desc;
    }
}
