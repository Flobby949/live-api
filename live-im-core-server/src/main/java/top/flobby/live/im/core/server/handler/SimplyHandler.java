package top.flobby.live.im.core.server.handler;

import io.netty.channel.ChannelHandlerContext;
import top.flobby.live.im.core.server.common.ImMsg;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 对消息进行简单处理，采用策略模式进行优化
 * @create : 2023-12-07 15:32
 **/

public interface SimplyHandler {

    /**
     * 处理消息
     *
     * @param ctx CTX
     * @param msg 消息
     */
    void handler(ChannelHandlerContext ctx, ImMsg msg);
}
