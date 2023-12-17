package top.flobby.live.framework.redis.starter.key;

import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 直播间模块
 * @create : 2023-12-05 13:10
 **/

@Configuration
@Conditional(RedisKeyLoadMatch.class)
public class GiftProviderCacheKeyBuilder extends RedisKeyBuilder {
    private static final String GIFT_LIST_KEY = "gift:list_cache";
    private static final String GIFT_OBJ_KEY = "gift:obj_cache";

    public String buildGiftListKey() {
        return super.getPrefix() + GIFT_LIST_KEY;
    }

    public String buildGiftObjKey(Integer giftId) {
        return super.getPrefix() + GIFT_OBJ_KEY +
                super.getSplitItem() + giftId;
    }
}
