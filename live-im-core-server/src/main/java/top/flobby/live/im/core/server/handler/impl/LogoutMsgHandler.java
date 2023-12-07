package top.flobby.live.im.core.server.handler.impl;

import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import top.flobby.live.im.core.server.common.ChannelHandlerContextCache;
import top.flobby.live.im.core.server.common.ImMsg;
import top.flobby.live.im.core.server.handler.SimplyHandler;
import top.flobby.live.im.core.server.utils.ImContextUtils;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 登出消息处理器
 * @create : 2023-12-07 15:33
 **/

@Slf4j
@Component
public class LogoutMsgHandler implements SimplyHandler {
    @Override
    public void handler(ChannelHandlerContext ctx, ImMsg msg) {
        Long userId = ImContextUtils.getUserId(ctx);

        if (ObjectUtils.isEmpty(userId)) {
            log.error("userId为空, 关闭连接, imMsg:{}", msg);
            throw new IllegalArgumentException("userId为空");
        }
        // 理想情况下，客户端断线时会发送断线消息包，但是防止客户端异常断线，所以从ctx中获取userId
        ChannelHandlerContextCache.remove(userId);
        ctx.close();
    }
}
