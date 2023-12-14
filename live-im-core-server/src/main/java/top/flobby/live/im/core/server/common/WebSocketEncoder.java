package top.flobby.live.im.core.server.common;

import com.alibaba.fastjson2.JSON;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

/**
 * @author : Flobby
 * @program : live-api
 * @description : ws消息解码
 * @create : 2023-12-14 13:50
 **/

public class WebSocketEncoder extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (!(msg instanceof ImMsg imMsg)) {
            super.write(ctx, msg, promise);
            return;
        }
        ctx.channel().writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(imMsg)));
    }
}
