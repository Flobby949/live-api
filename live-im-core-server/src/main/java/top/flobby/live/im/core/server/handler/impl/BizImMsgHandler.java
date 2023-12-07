package top.flobby.live.im.core.server.handler.impl;

import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.flobby.live.im.core.server.common.ImMsg;
import top.flobby.live.im.core.server.handler.SimplyHandler;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 常规业务消息处理器
 * @create : 2023-12-07 15:33
 **/

@Slf4j
@Component
public class BizImMsgHandler implements SimplyHandler {
    @Override
    public void handler(ChannelHandlerContext ctx, ImMsg msg) {
        System.out.println("业务消息处理器, msg:" + msg);
        ctx.writeAndFlush(msg);
    }
}
