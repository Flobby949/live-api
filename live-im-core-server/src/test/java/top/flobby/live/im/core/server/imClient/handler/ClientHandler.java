package top.flobby.live.im.core.server.imClient.handler;

import com.alibaba.fastjson2.JSON;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import top.flobby.live.im.common.ImMsgCodeEnum;
import top.flobby.live.im.core.server.common.ImMsg;
import top.flobby.live.im.dto.ImMsgBody;

/**
 * @author : Flobby
 * @program : live-api
 * @description :
 * @create : 2023-12-07 15:48
 **/

public class ClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ImMsg imMsg = (ImMsg) msg;
        if (imMsg.getCode() == ImMsgCodeEnum.IM_BUSINESS_MSG.getCode()) {
            ImMsgBody respBody = JSON.parseObject(imMsg.getBody(), ImMsgBody.class);
            ImMsgBody ackBody = ImMsgBody.builder()
                    .userId(respBody.getUserId())
                    .msgId(respBody.getMsgId())
                    .appId(respBody.getAppId())
                    .build();
            ImMsg ackMsg = ImMsg.build(ImMsgCodeEnum.IM_ACK_MSG, JSON.toJSONString(ackBody));
            ctx.writeAndFlush(ackMsg);
        }
        System.out.println("【服务端响应】收到服务端消息：" + new String(imMsg.getBody()));
    }
}
