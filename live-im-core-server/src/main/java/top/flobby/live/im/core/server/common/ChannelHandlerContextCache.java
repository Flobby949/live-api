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

    /**
     * 当前IM服务启动的时候，对外暴露的IP地址和端口
     */
    private static String SERVER_IP_ADDRESS;

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

    public static void setServerAddress(String ip, String port) {
        SERVER_IP_ADDRESS = ip + ":" + port;
    }

    public static String getServerAddress() {
        return SERVER_IP_ADDRESS;
    }
}
