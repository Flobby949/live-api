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
    public static final String USER_TAG_LOCK_KEY = "userTagLock";

    public String buildUserInfoKey(Long userId) {
        return super.getPrefix() + USER_INFO_KEY +
                super.getSplitItem() + userId;
    }

    public String buildUserTagLockKey(Long userId) {
        return super.getPrefix() + USER_TAG_LOCK_KEY +
                super.getSplitItem() + userId;
    }
}
