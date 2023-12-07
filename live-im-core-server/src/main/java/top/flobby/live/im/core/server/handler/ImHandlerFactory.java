package top.flobby.live.im.core.server.handler;

import io.netty.channel.ChannelHandlerContext;
import top.flobby.live.im.core.server.common.ImMsg;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 消息处理器工厂，消息处理分发
 * @create : 2023-12-07 15:36
 **/

public interface ImHandlerFactory {


    /**
     * 消息处理分发
     *
     * @param ctx CTX
     * @param msg 消息
     */
    void doMsgHandler(ChannelHandlerContext ctx, ImMsg msg);
}
