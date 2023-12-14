package top.flobby.live.im.core.server.handler.ws;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import top.flobby.live.common.utils.JwtUtil;
import top.flobby.live.im.core.server.handler.impl.LoginMsgHandler;
import top.flobby.live.im.interfaces.ImTokenRpc;

import java.net.URI;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 握手连接处理器
 * @create : 2023-12-13 17:37
 **/

@Slf4j
@Component
@ChannelHandler.Sharable
public class WsSharkHandler extends ChannelInboundHandlerAdapter {

    @Value("${live.im.ws.port}")
    private int port;
    @Value("${spring.cloud.nacos.discovery.ip}")
    private String serverIp;

    @DubboReference
    private ImTokenRpc imTokenRpc;
    @Resource
    private LoginMsgHandler loginMsgHandler;

    private WebSocketServerHandshaker handShaker;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("[WsSharkHandler] 开始处理ws消息");
        // 握手接入ws
        if (msg instanceof FullHttpRequest) {
            handleHttpRequest(ctx, (FullHttpRequest) msg);
            return;
        }
        // 正常关闭链路
        if (msg instanceof CloseWebSocketFrame) {
            log.info("[WsSharkHandler] 正常关闭ws连接");
            handShaker.close(ctx.channel(), ((CloseWebSocketFrame) msg).retain());
            return;
        }
        // 将消息传递到下一个链路
        ctx.fireChannelRead(msg);
    }

    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest msg) {
        log.info("[WsSharkHandler] 开始进行ws握手");
        String webSocketUrl = "ws://" + serverIp + ":" + port;
        // 构造握手返回
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(webSocketUrl, null, false);
        String uri = msg.uri();
        // ws://127.0.0.1:port?token=xxx&userId=xxx&roomId=xxx，分别取出token、userId、roomId
        String query = URI.create(uri).getQuery();
        String[] pairs = query.split("&");
        String token = null;
        Long userId = null;
        Long roomId = null;
        for (String pair : pairs) {
            String[] split = pair.split("=");
            if ("token".equals(split[0])) {
                token = split[1];
            } else if ("userId".equals(split[0])) {
                userId = Long.parseLong(split[1]);
            } else if ("roomId".equals(split[0])) {
                roomId = Long.parseLong(split[1]);
            }
        }
        Long queryUserId = imTokenRpc.getUserIdByToken(token);
        if (ObjectUtils.isEmpty(queryUserId) || !queryUserId.equals(userId)) {
            // token无效，关闭连接
            log.warn("[WsSharkHandler] token is invalid, token is {}", token);
            ctx.close();
            return;
        }
        // 建立ws的握手连接
        handShaker = wsFactory.newHandshaker(msg);

        if (handShaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
            return;
        }
        ChannelFuture channelFuture = handShaker.handshake(ctx.channel(), msg);
        // 首次握手成功，返回握手成功消息
        if (channelFuture.isSuccess()) {
            // token中保存的就是appId
            Integer appId = JwtUtil.getJSONObject(token).getInt("appId");
            // TODO roomId
            loginMsgHandler.loginSuccessHandler(ctx, userId, appId, roomId);
            log.info("[WsSharkHandler] handshake success, userId is {}", userId);
        }
    }
}
