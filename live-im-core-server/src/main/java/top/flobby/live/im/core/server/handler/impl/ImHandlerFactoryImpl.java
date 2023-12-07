package top.flobby.live.im.core.server.handler.impl;

import io.netty.channel.ChannelHandlerContext;
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

public class ImHandlerFactoryImpl implements ImHandlerFactory {

    private static Map<Integer, SimplyHandler> simplyHandlerMap = new HashMap<>(4);

    static {
        simplyHandlerMap.put(ImMsgCodeEnum.IM_LOGIN_MSG.getCode(), new LoginMsgHandler());
        simplyHandlerMap.put(ImMsgCodeEnum.IM_LOGOUT_MSG.getCode(), new LogoutMsgHandler());
        simplyHandlerMap.put(ImMsgCodeEnum.IM_BUSINESS_MSG.getCode(), new BizImMsgHandler());
        simplyHandlerMap.put(ImMsgCodeEnum.IM_HEARTBEAT_MSG.getCode(), new HeartBeatMsgHandler());
    }

    @Override
    public void doMsgHandler(ChannelHandlerContext ctx, ImMsg msg) {
        int code = msg.getCode();
        SimplyHandler simplyHandler = simplyHandlerMap.get(code);
        if (simplyHandler == null) {
            throw new IllegalArgumentException("no handler found for code : " + code);
        }
        simplyHandler.handler(ctx, msg);
    }
}
