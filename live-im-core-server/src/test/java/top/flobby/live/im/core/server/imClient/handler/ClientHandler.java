package top.flobby.live.im.core.server.imClient.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import top.flobby.live.im.core.server.common.ImMsg;

/**
 * @author : Flobby
 * @program : live-api
 * @description :
 * @create : 2023-12-07 15:48
 **/

public class ClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ImMsg imMsg = (ImMsg) msg;
        System.out.println("【服务端响应】收到服务端消息：" + imMsg);
    }
}
