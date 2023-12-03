package top.flobby.live.framework.redis.starter.key;

import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 用户中台的key
 * @create : 2023-11-19 13:41
 **/

@Configuration
@Conditional(RedisKeyLoadMatch.class)
public class UserProviderCacheKeyBuilder extends RedisKeyBuilder {
    private static final String USER_INFO_KEY = "userInfo";
    public static final String USER_TAG_KEY = "userTag";
    public static final String USER_TAG_LOCK_KEY = "userTagLock";

    public static final String USER_PHONE_LIST_KEY = "userPhoneList";
    public static final String USER_PHONE_OBJ_KEY = "userPhoneObj";
    public static final String USER_LOGIN_TOKEN_KEY = "userLoginToken";

    public String buildUserInfoKey(Long userId) {
        return super.getPrefix() + USER_INFO_KEY +
                super.getSplitItem() + userId;
    }

    public String buildUserTagLockKey(Long userId) {
        return super.getPrefix() + USER_TAG_LOCK_KEY +
                super.getSplitItem() + userId;
    }

    public String buildUserTagKey(Long userId) {
        return super.getPrefix() + USER_TAG_KEY +
                super.getSplitItem() + userId;
    }

    public String buildUserPhoneListKey(Long userId) {
        return super.getPrefix() + USER_PHONE_LIST_KEY +
                super.getSplitItem() + userId;
    }

    public String buildUserPhoneObjKey(String phone) {
        return super.getPrefix() + USER_PHONE_OBJ_KEY +
                super.getSplitItem() + phone;
    }

    public String buildUserLoginTokenKey(String token) {
        return super.getPrefix() + USER_LOGIN_TOKEN_KEY +
                super.getSplitItem() + token;
    }
}
