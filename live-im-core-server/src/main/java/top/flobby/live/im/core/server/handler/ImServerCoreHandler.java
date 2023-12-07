package top.flobby.live.im.core.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import top.flobby.live.im.core.server.common.ImMsg;
import top.flobby.live.im.core.server.handler.impl.ImHandlerFactoryImpl;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 基本处理器
 * @create : 2023-12-07 15:24
 **/

public class ImServerCoreHandler extends SimpleChannelInboundHandler<Object> {

    private final ImHandlerFactory imHandlerFactory = new ImHandlerFactoryImpl();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        // instanceof 增强写法，判断是否是ImMsg类型
        if (!(msg instanceof ImMsg imMsg)) {
            throw new IllegalArgumentException("msg is not instance of ImMsg, msg is " + msg.getClass().getName());
        }
        // 根据code，分发到不同的handler处理
        // 登录消息包，登录 token 认证，channel和userId的映射关系
        // 登出消息包，正常断开im连接时发送
        // 业务消息包，常用消息类型，发送接收消息时使用
        // 心跳消息包，定时给im发送，汇报功能，用于保持长连接
        imHandlerFactory.doMsgHandler(ctx, imMsg);
    }
}
