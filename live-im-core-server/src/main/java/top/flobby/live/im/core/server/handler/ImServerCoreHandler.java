package top.flobby.live.im.core.server.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import top.flobby.live.im.core.server.common.ImMsg;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 基本处理器
 * @create : 2023-12-07 15:24
 **/

@Component
@ChannelHandler.Sharable
public class ImServerCoreHandler extends SimpleChannelInboundHandler<Object> {

    @Resource
    private ImHandlerFactory imHandlerFactory;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        // instanceof 增强写法，判断是否是ImMsg类型
        if (!(msg instanceof ImMsg imMsg)) {
            throw new IllegalArgumentException("msg is not instance of ImMsg, msg is " + msg.getClass().getName());
        }
        // 根据code，分发到不同的handler处理
        imHandlerFactory.doMsgHandler(ctx, imMsg);
    }
}
