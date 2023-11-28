package top.flobby.live.user.constants;

import lombok.Getter;

import static top.flobby.live.user.constants.Constant.TAG_INFO_01;

/**
 * @author : Flobby
 * @program : live-api
 * @description :
 * @create : 2023-11-28 12:04
 **/

@Getter
public enum UserTagsEnum {

    /**
     * 在 2 的 n 次方中，n 为枚举的顺序，从 0 开始
     */
    IS_RICH((long) Math.pow(2, 0), "是否是金主用户", TAG_INFO_01),
    IS_VIP((long) Math.pow(2, 1), "是否是VIP用户", TAG_INFO_01),
    IS_OLD_USER((long) Math.pow(2, 2), "是否是老用户", TAG_INFO_01),
    ;

    final Long tag;
    final String desc;
    final String fieldName;

    UserTagsEnum(Long tag, String desc, String fieldName) {
        this.tag = tag;
        this.desc = desc;
        this.fieldName = fieldName;
    }
}
