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
    public static final String GIFT_CONSUME_LOCK_KEY = "gift:consume_lock_key";
    public static final String LIVING_PK_IS_OVER = "living_pk_is_over";
    public static final String RED_PACKET_LIST_KEY = "red_packet:list_cache";
    public static final String RED_PACKET_LIST_INIT_LOCK = "red_packet:list_init_lock";

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

    public String buildLivingPkKey(Integer roomId) {
        return super.getPrefix() + LIVING_PK_KEY + super.getSplitItem() + roomId;
    }

    public String buildLivingPkSendSeq(Integer roomId) {
        return super.getPrefix() + LIVING_PK_SEND_SEQ + super.getSplitItem() + roomId;
    }

    public String buildGiftConsumeLockKey() {
        return super.getPrefix() + GIFT_CONSUME_LOCK_KEY;
    }

    public String buildLivingPkIsOverKey(Integer roomId) {
        return super.getPrefix() + LIVING_PK_IS_OVER + super.getSplitItem() + roomId;
    }

    public String buildRedPacketListKey(String code) {
        return super.getPrefix() + RED_PACKET_LIST_KEY + super.getSplitItem() + code;
    }

    public String buildRedPacketListInitLock(String code) {
        return super.getPrefix() + RED_PACKET_LIST_INIT_LOCK + super.getSplitItem() + code;
    }
}
