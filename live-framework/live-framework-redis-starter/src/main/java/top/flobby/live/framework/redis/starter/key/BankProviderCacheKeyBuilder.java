package top.flobby.live.framework.redis.starter.key;

import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 银行模块
 * @create : 2023-12-05 13:10
 **/

@Configuration
@Conditional(RedisKeyLoadMatch.class)
public class BankProviderCacheKeyBuilder extends RedisKeyBuilder {
    private static final String BALANCE_CACHE = "balance_cache";

    /**
     * 构建用户余额密钥
     *
     * @param userId 用户 ID
     * @return {@link String}
     */
    public String buildUserBalanceKey(Long userId) {
        return super.getPrefix() + BALANCE_CACHE +
                super.getSplitItem() + userId;
    }

}
