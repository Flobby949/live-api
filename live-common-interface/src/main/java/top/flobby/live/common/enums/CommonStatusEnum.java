package top.flobby.live.common.enums;

import lombok.Getter;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 通用状态枚举
 * @create : 2023-12-12 10:12
 **/

@Getter
public enum CommonStatusEnum {
    VALID(1, "有效"),
    INVALID(0, "无效"),
    ;
    final Byte code;
    final String desc;

    CommonStatusEnum(Integer code, String desc) {
        this.code = code.byteValue();
        this.desc = desc;
    }
}
