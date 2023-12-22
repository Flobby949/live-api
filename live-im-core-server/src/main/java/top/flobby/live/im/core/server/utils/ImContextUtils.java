package top.flobby.live.im.core.server.utils;

import io.netty.channel.ChannelHandlerContext;
import top.flobby.live.im.core.server.common.ImContextAttr;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 工具类
 * @create : 2023-12-07 18:04
 **/

public class ImContextUtils {

    /**
     * 获取用户 ID
     *
     * @param ctx CTX
     * @return {@link Long}
     */
    public static Long getUserId(ChannelHandlerContext ctx) {
        return ctx.attr(ImContextAttr.USER_ID).get();
    }

    /**
     * 设置用户 ID
     *
     * @param ctx    CTX
     * @param userId 用户 ID
     */
    public static void setUserId(ChannelHandlerContext ctx, Long userId) {
        ctx.attr(ImContextAttr.USER_ID).set(userId);
    }

    /**
     * 获取应用 ID
     *
     * @param ctx CTX
     * @return {@link Integer}
     */
    public static Integer getAppId(ChannelHandlerContext ctx) {
        return ctx.attr(ImContextAttr.APP_ID).get();
    }

    /**
     * 设置应用 ID
     *
     * @param ctx   CTX
     * @param appId 应用 ID
     */
    public static void setAppId(ChannelHandlerContext ctx, Integer appId) {
        ctx.attr(ImContextAttr.APP_ID).set(appId);
    }

    /**
     * 获取房间 ID
     *
     * @param ctx CTX
     * @return {@link Integer}
     */
    public static Integer getRoomId(ChannelHandlerContext ctx) {
        return ctx.attr(ImContextAttr.ROOM_ID).get();
    }

    public static void setRoomId(ChannelHandlerContext ctx, Integer roomId) {
        ctx.attr(ImContextAttr.ROOM_ID).set(roomId);
    }

    /**
     * 删除用户 ID
     *
     * @param ctx CTX
     */
    public static void removeUserId(ChannelHandlerContext ctx) {
        ctx.attr(ImContextAttr.USER_ID).remove();
    }

    /**
     * 删除 App ID
     *
     * @param ctx CTX
     */
    public static void removeAppId(ChannelHandlerContext ctx) {
        ctx.attr(ImContextAttr.APP_ID).remove();
    }
}
