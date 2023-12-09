package top.flobby.live.im.core.server.service.impl;

import com.alibaba.fastjson2.JSON;
import io.netty.channel.ChannelHandlerContext;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import top.flobby.live.im.common.ImMsgCodeEnum;
import top.flobby.live.im.core.server.common.ChannelHandlerContextCache;
import top.flobby.live.im.core.server.common.ImMsg;
import top.flobby.live.im.core.server.service.IMsgAckCheckService;
import top.flobby.live.im.core.server.service.IRouterHandlerService;
import top.flobby.live.im.dto.ImMsgBody;

import java.util.UUID;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 实现
 * @create : 2023-12-08 13:44
 **/

@Service
public class RouterHandlerServiceImpl implements IRouterHandlerService {

    @Resource
    private IMsgAckCheckService msgAckCheckService;

    @Override
    public void onReceive(ImMsgBody msgBody) {
        if (sendMsgToClient(msgBody)) {
            // 当 Im 服务器推送消息到客户端后，记录消息的 ACK
            msgAckCheckService.recordAckMsg(msgBody, 1);
            msgAckCheckService.sendDelayMsg(msgBody);
        }
    }

    @Override
    public boolean sendMsgToClient(ImMsgBody msgBody) {
        // 接收消息通知的userId
        Long userId = msgBody.getUserId();
        ChannelHandlerContext ctx = ChannelHandlerContextCache.get(userId);
        if (ctx != null) {
            String msgId = UUID.randomUUID().toString();
            msgBody.setMsgId(msgId);
            ImMsg respMsg = ImMsg.build(ImMsgCodeEnum.IM_BUSINESS_MSG, JSON.toJSONString(msgBody));
            // 回写消息到客户端
            ctx.writeAndFlush(respMsg);
            return true;
        }
        return false;
    }

}
