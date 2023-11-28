package top.flobby.live.user.provider.service;

import top.flobby.live.user.constants.UserTagsEnum;

/**
 * @author : Flobby
 * @program : live-api
 * @description : service
 * @create : 2023-11-28 12:02
 **/

public interface IUserTagService {
    /**
     * 设置标签
     *
     * @param userId       用户 ID
     * @param userTagsEnum 用户标签枚举
     * @return boolean
     */
    boolean setTag(Long userId, UserTagsEnum userTagsEnum);

    /**
     * 取消标签
     *
     * @param userId       用户 ID
     * @param userTagsEnum 用户标签枚举
     * @return boolean
     */
    boolean cancelTag(Long userId, UserTagsEnum userTagsEnum);

    /**
     * 是否包含标签
     *
     * @param userId       用户 ID
     * @param userTagsEnum 用户标签枚举
     * @return boolean
     */
    boolean containsTag(Long userId, UserTagsEnum userTagsEnum);
}
