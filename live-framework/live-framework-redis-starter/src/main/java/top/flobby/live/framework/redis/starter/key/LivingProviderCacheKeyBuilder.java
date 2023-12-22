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
public class LivingProviderCacheKeyBuilder extends RedisKeyBuilder {
    private static final String LIVING_ROOM_LIST_KEY = "living:room:list";
    private static final String LIVING_ROOM_OBJ_KEY = "living:room:obj";
    private static final String LIVING_ROOM_LOCK_KEY = "living:room:lock";
    public static final String LIVING_ROOM_ONLINE_SET_KEY = "living:room:online";
    public static final String LIVING_ROOM_ONLINE_PK_KEY = "living:room:online_pk";

    public String buildLivingRoomListKey(Integer type) {
        return super.getPrefix() + LIVING_ROOM_LIST_KEY +
                super.getSplitItem() + type;
    }

    public String buildLivingRoomObjKey(Integer roomId) {
        return super.getPrefix() + LIVING_ROOM_OBJ_KEY +
                super.getSplitItem() + roomId;
    }

    public String buildRefreshRoomListLock() {
        return super.getPrefix() + LIVING_ROOM_LOCK_KEY;
    }

    public String buildLivingRoomUserKey(Integer roomId, Integer appId) {
        return super.getPrefix() + LIVING_ROOM_ONLINE_SET_KEY +
                super.getSplitItem() + appId +
                super.getSplitItem() + roomId;
    }

    public String buildLivingRoomOnlinePkKey(Integer roomId) {
        return super.getPrefix() + LIVING_ROOM_ONLINE_PK_KEY +
                super.getSplitItem() + roomId;
    }
}
