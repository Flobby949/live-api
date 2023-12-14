package top.flobby.live.im.core.server.handler.ws;

import com.alibaba.fastjson2.JSONObject;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import top.flobby.live.common.constants.ImCoreServerConstant;
import top.flobby.live.im.core.server.common.ChannelHandlerContextCache;
import top.flobby.live.im.core.server.common.ImMsg;
import top.flobby.live.im.core.server.handler.ImHandlerFactory;
import top.flobby.live.im.core.server.utils.ImContextUtils;

/**
 * @author : Flobby
 * @program : live-api
 * @description : webSocket消息统一处理
 * @create : 2023-12-13 17:18
 **/

@Slf4j
@Component
@ChannelHandler.Sharable
public class WsImServerCoreHandler extends SimpleChannelInboundHandler {

    @Resource
    private ImHandlerFactory imHandlerFactory;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("[WsImServerCoreHandler] 开始处理ws消息");
        if (msg instanceof WebSocketFrame) {
            wsMsgHandler(ctx, (WebSocketFrame) msg);
        }
    }

    private void wsMsgHandler(ChannelHandlerContext ctx, WebSocketFrame msg) {
        // 如果不是文本消息，统一后台不会处理
        if (!(msg instanceof TextWebSocketFrame)) {
            log.warn("[WsImServerCoreHandler] msg is not instance of TextWebSocketFrame, msg is {}", msg.getClass().getName());
            return;
        }
        log.info("[WsImServerCoreHandler] 开始处理文本消息");
        try {
            // 返回应答消息
            String content = ((TextWebSocketFrame) msg).text();
            JSONObject jsonObject = JSONObject.parseObject(content);
            ImMsg imMsg = new ImMsg();
            imMsg.setMagic(jsonObject.getShort("magic"));
            imMsg.setCode(jsonObject.getInteger("code"));
            imMsg.setLen(jsonObject.getInteger("len"));
            imMsg.setBody(jsonObject.getString("body").getBytes());
            imHandlerFactory.doMsgHandler(ctx, imMsg);
        } catch (Exception e) {
            log.error("[WsImServerCoreHandler] msg handler error, msg is {}", msg);
        }
    }

    /**
     * 通道处于非活动状态
     *
     * @param ctx CTX
     * @throws Exception 例外
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("[WsImServerCoreHandler] 连接掉线");
        Long userId = ImContextUtils.getUserId(ctx);
        Integer appId = ImContextUtils.getAppId(ctx);
        if (ObjectUtils.isEmpty(userId) && ObjectUtils.isEmpty(appId)) {
            ChannelHandlerContextCache.remove(userId);
            // 移除断线的ip连接记录
            redisTemplate.delete(ImCoreServerConstant.IM_BIND_IP_KEY + appId + ":" + userId);
        }
    }
}
