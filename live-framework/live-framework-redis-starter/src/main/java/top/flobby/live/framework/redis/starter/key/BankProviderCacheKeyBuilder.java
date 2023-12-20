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

    private static final String PAY_PRODUCT_LIST_CACHE = "pay_product_list_cache";

    private static final String PAY_PRODUCT_ITEM_CACHE = "pay_product_item_cache";

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

    /**
     * 构建付费产品列表密钥
     *
     * @param type 类型
     * @return {@link String}
     */
    public String buildPayProductListKey(Integer type) {
        return super.getPrefix() + PAY_PRODUCT_LIST_CACHE +
                super.getSplitItem() + type;
    }

    /**
     * 构建付费产品密钥
     *
     * @param productId 产品 ID
     * @return {@link String}
     */
    public String buildPayProductItemKey(Integer productId) {
        return super.getPrefix() + PAY_PRODUCT_ITEM_CACHE +
                super.getSplitItem() + productId;
    }

}
