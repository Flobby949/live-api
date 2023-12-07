package top.flobby.live.im.core.server.handler.impl;

import io.netty.channel.ChannelHandlerContext;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import top.flobby.live.im.common.ImMsgCodeEnum;
import top.flobby.live.im.core.server.common.ImMsg;
import top.flobby.live.im.core.server.handler.ImHandlerFactory;
import top.flobby.live.im.core.server.handler.SimplyHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 实现类
 * @create : 2023-12-07 15:37
 **/

@Component
public class ImHandlerFactoryImpl implements ImHandlerFactory, InitializingBean {

    public static final Map<Integer, SimplyHandler> SIMPLY_HANDLER_MAP = new HashMap<>();
    @Resource
    private ApplicationContext applicationContext;

    @Override
    public void doMsgHandler(ChannelHandlerContext ctx, ImMsg msg) {
        int code = msg.getCode();
        SimplyHandler simplyHandler = SIMPLY_HANDLER_MAP.get(code);
        if (simplyHandler == null) {
            throw new IllegalArgumentException("no handler found for code : " + code);
        }
        simplyHandler.handler(ctx, msg);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // 登录消息包，登录 token 认证，channel和userId的映射关系
        SIMPLY_HANDLER_MAP.put(ImMsgCodeEnum.IM_LOGIN_MSG.getCode(),
                applicationContext.getBean(LoginMsgHandler.class));
        // 登出消息包，正常断开im连接时发送
        SIMPLY_HANDLER_MAP.put(ImMsgCodeEnum.IM_LOGOUT_MSG.getCode(),
                applicationContext.getBean(LogoutMsgHandler.class));
        // 业务消息包，常用消息类型，发送接收消息时使用
        SIMPLY_HANDLER_MAP.put(ImMsgCodeEnum.IM_BUSINESS_MSG.getCode(),
                applicationContext.getBean(BizImMsgHandler.class));
        // 心跳消息包，定时给im发送，汇报功能，用于保持长连接
        SIMPLY_HANDLER_MAP.put(ImMsgCodeEnum.IM_HEARTBEAT_MSG.getCode(),
                applicationContext.getBean(HeartBeatMsgHandler.class));
    }
}
