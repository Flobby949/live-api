package top.flobby.live.im.core.server.handler.impl;

import com.alibaba.fastjson2.JSON;
import io.netty.channel.ChannelHandlerContext;
import top.flobby.live.im.core.server.common.ImMsg;
import top.flobby.live.im.core.server.handler.SimplyHandler;
import top.flobby.live.im.dto.ImMsgBody;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 登录消息处理器
 * @create : 2023-12-07 15:33
 **/

public class LoginMsgHandler implements SimplyHandler {
    @Override
    public void handler(ChannelHandlerContext ctx, ImMsg msg) {
        byte[] body = msg.getBody();
        ImMsgBody imMsgBody = JSON.parseObject(body, ImMsgBody.class);
        String token = imMsgBody.getToken();
        System.out.println("登录消息处理器, msg:" + msg);
        ctx.writeAndFlush(msg);
    }
}
