package top.flobby.live.user.constants;

import lombok.Getter;

/**
 * @author : Flobby
 * @program : live-api
 * @description : MQ 消息 code 枚举
 * @create : 2023-11-29 14:42
 **/

@Getter
public enum CacheAsyncDeleteEnum {

    USER_INFO_DELETE(1, "删除用户信息"),
    USER_TAG_DELETE(2, "删除用户标签");

    final Integer code;
    final String desc;

    CacheAsyncDeleteEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    // 根据 code 获取枚举desc
    public static String getDescByCode(Integer code) {
        for (CacheAsyncDeleteEnum value : CacheAsyncDeleteEnum.values()) {
            if (value.getCode().equals(code)) {
                return value.getDesc();
            }
        }
        return null;
    }
}
