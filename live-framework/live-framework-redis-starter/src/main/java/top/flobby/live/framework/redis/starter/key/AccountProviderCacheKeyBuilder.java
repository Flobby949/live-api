package top.flobby.live.framework.redis.starter.key;

import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 账户模块
 * @create : 2023-12-05 13:10
 **/

@Configuration
@Conditional(RedisKeyLoadMatch.class)
public class AccountProviderCacheKeyBuilder extends RedisKeyBuilder {
    private static final String ACCOUNT_TOKEN_KEY = "account";

    public String buildAccountTokenKey(String phone) {
        return super.getPrefix() + ACCOUNT_TOKEN_KEY +
                super.getSplitItem() + phone;
    }
}
