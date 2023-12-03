package top.flobby.live.id.enums;

import lombok.Getter;

/**
 * @author : Flobby
 * @program : live-api
 * @description : ID生成策略枚举
 * @create : 2023-12-03 15:05
 **/

@Getter
public enum IdTypeEnum {

    USER_ID(1, "用户ID生成策略"),
    ;

    final int code;
    final String desc;

    IdTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
