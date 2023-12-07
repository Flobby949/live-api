package top.flobby.live.im.common;

import lombok.Getter;

/**
 * @author : Flobby
 * @program : live-api
 * @description : APP ID 枚举
 * @create : 2023-12-07 17:07
 **/

@Getter
public enum AppIdEnum {

    LIVE_BIZ_ID(1, "直播业务"),
    ;

    final Integer code;
    final String desc;

    AppIdEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
