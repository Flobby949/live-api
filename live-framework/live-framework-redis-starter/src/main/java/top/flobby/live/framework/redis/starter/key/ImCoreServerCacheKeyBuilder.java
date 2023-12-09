package top.flobby.live.framework.redis.starter.key;

import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * @author : Flobby
 * @program : live-api
 * @description : IM模块
 * @create : 2023-12-07 16:58
 **/

@Configuration
@Conditional(RedisKeyLoadMatch.class)
public class ImCoreServerCacheKeyBuilder extends RedisKeyBuilder {
    private static final String IM_ONLINE_ZSET = "ImOnlineZSet";
    public static final String IM_ACK_MSG_MAP = "ImAckMsgMap";

    /**
     * 按照用户id取模10000
     *
     * @param userId 用户 ID
     * @return {@link String}
     */
    public String buildImOnlineZSetKey(Long userId, Integer appId) {
        return super.getPrefix() + IM_ONLINE_ZSET +
                super.getSplitItem() + appId +
                super.getSplitItem() + userId % 10000;
    }

    /**
     * 构建 im ack 密钥
     *
     * @param userId 用户 ID
     * @param appId  应用 ID
     * @return {@link String}
     */
    public String buildImAckMsgMapKey(Long userId, Integer appId) {
        return super.getPrefix() + IM_ACK_MSG_MAP +
                super.getSplitItem() + appId +
                super.getSplitItem() + userId % 100;
    }
}
