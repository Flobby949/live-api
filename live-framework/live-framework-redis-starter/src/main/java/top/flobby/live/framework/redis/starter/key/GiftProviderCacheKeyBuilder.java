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
    public static final String RED_PACKET_LIST_PREPARE_DONE = "red_packet:list_prepare_done";
    public static final String RED_PACKET_TOTAL_GET_CACHE = "red_packet:total_get_cache";
    public static final String RED_PACKET_TOTAL_GET_PRICE = "red_packet:total_get_price";
    public static final String RED_PACKET_MAX_GET_PRICE_CACHE = "red_packet:max_get_price_cache";
    public static final String RED_PACKET_USER_TOTAL_PRICE = "red_packet:user_total_price";
    public static final String RED_PACKET_NOTIFY = "red_packet:notify";
    public static final String SKU_DETAIL_KEY = "sku:detail";
    public static final String SKU_STOCK_KEY = "sku:stock";
    private static final String SHOP_CART = "shop_cart";
    private static final String SKU_SYNC_LOCK = "sku:sync_lock";
    private static final String USER_SKU_ORDER = "sku:user_order";

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

    public String buildRedPacketListPrepareDone(String code) {
        return super.getPrefix() + RED_PACKET_LIST_PREPARE_DONE + super.getSplitItem() + code;
    }

    public String buildRedPacketTotalGetCache(String code) {
        return super.getPrefix() + RED_PACKET_TOTAL_GET_CACHE + super.getSplitItem() + code;
    }

    public String buildRedPacketTotalGetPrice(String code) {
        return super.getPrefix() + RED_PACKET_TOTAL_GET_PRICE + super.getSplitItem() + code;
    }

    public String buildRedPacketMaxGetPriceCache(String code) {
        return super.getPrefix() + RED_PACKET_MAX_GET_PRICE_CACHE + super.getSplitItem() + code;
    }

    public String buildRedPacketUserTotalPrice(String code, Long userId) {
        return super.getPrefix() + RED_PACKET_USER_TOTAL_PRICE + super.getSplitItem() + code + super.getSplitItem() + userId;
    }

    public String buildRedPacketNotify(String code) {
        return super.getPrefix() + RED_PACKET_NOTIFY + super.getSplitItem() + code;
    }

    public String buildSkuDetail(Long skuId) {
        return super.getPrefix() + SKU_DETAIL_KEY + super.getSplitItem() + skuId;
    }

    public String buildUserShopCar(Long userId, Integer roomId) {
        return super.getPrefix() + SHOP_CART + super.getSplitItem() + userId + super.getSplitItem() + roomId;
    }

    public String buildSkuStock(Long skuId) {
        return super.getPrefix() + SKU_STOCK_KEY + super.getSplitItem() + skuId;
    }

    public String buildSkuSyncLock() {
        return super.getPrefix() + SKU_SYNC_LOCK;
    }

    public String buildUserSkuOrder(Long userId, Integer roomId) {
        return super.getPrefix() + USER_SKU_ORDER + super.getSplitItem() + userId + super.getSplitItem() + roomId;
    }
}
