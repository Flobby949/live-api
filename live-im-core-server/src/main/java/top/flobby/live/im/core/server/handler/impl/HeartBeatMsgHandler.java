package top.flobby.live.im.core.server.handler.impl;

import io.netty.channel.ChannelHandlerContext;
import top.flobby.live.im.core.server.common.ImMsg;
import top.flobby.live.im.core.server.handler.SimplyHandler;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 心跳消息处理器
 * @create : 2023-12-07 15:33
 **/

public class HeartBeatMsgHandler implements SimplyHandler {
    @Override
    public void handler(ChannelHandlerContext ctx, ImMsg msg) {
        System.out.println("心跳消息处理器, msg:" + msg);
        ctx.writeAndFlush(msg);
    }
}
