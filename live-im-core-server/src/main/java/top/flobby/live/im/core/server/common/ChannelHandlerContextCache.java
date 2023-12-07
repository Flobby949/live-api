package top.flobby.live.im.core.server.common;

import io.netty.channel.ChannelHandlerContext;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : Flobby
 * @program : live-api
 * @description : ctx 管理
 * @create : 2023-12-07 17:52
 **/

public class ChannelHandlerContextCache {

    private static Map<Long, ChannelHandlerContext> ctxMap = new HashMap<>();

    public static ChannelHandlerContext get(Long userId) {
        return ctxMap.get(userId);
    }

    public static void put(Long userId, ChannelHandlerContext ctx) {
        ctxMap.put(userId, ctx);
    }

    public static void remove(Long userId) {
        ctxMap.remove(userId);
    }
}
