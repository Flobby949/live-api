package top.flobby.live.common.constants;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 礼物模块topic维护
 * @create : 2023-12-17 16:27
 **/

public interface GiftProviderTopicNamesConstant {

    String REMOVE_GIFT_CACHE = "remove-gift-cache";

    String SEND_GIFT = "send-gift";
    String RECEIVE_RED_PACKET = "receive-red-packet";

    /**
     * 延迟回调，回滚库存
     */
    String ROLL_BACK_STOCK = "roll-back-stock";

    String START_LIVING_ROOM_SYNC_STOCK = "start-living-room-sync-stock";
}
