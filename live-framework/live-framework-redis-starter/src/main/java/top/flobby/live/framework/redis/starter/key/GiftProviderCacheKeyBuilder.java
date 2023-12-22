package top.flobby.live.framework.redis.starter.key;

import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 礼物模块
 * @create : 2023-12-05 13:10
 **/

@Configuration
@Conditional(RedisKeyLoadMatch.class)
public class GiftProviderCacheKeyBuilder extends RedisKeyBuilder {
    private static final String GIFT_LIST_KEY = "gift:list_cache";
    private static final String GIFT_OBJ_KEY = "gift:obj_cache";
    private static final String GIFT_CONSUME_KEY = "gift:consume_key";
    private static final String LIVING_PK_KEY = "living_pk_key";
    private static final String LIVING_PK_SEND_SEQ = "living_pk_send_seq";

    public String buildGiftListKey() {
        return super.getPrefix() + GIFT_LIST_KEY;
    }

    public String buildGiftObjKey(Integer giftId) {
        return super.getPrefix() + GIFT_OBJ_KEY +
                super.getSplitItem() + giftId;
    }

    public String buildGiftConsumeKey(String uuid) {
        return super.getPrefix() + GIFT_CONSUME_KEY +
                super.getSplitItem() + uuid;
    }

    public String buildLivingPkKey(Long roomId) {
        return super.getPrefix() + LIVING_PK_KEY + super.getSplitItem() + roomId;
    }

    public String buildLivingPkSendSeq(Long roomId) {
        return super.getPrefix() + LIVING_PK_SEND_SEQ + super.getSplitItem() + roomId;
    }
}
