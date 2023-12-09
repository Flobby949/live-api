package top.flobby.live.im.core.server.service.impl;

import com.alibaba.fastjson2.JSON;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Service;
import top.flobby.live.im.common.ImMsgCodeEnum;
import top.flobby.live.im.core.server.common.ChannelHandlerContextCache;
import top.flobby.live.im.core.server.common.ImMsg;
import top.flobby.live.im.core.server.service.IRouterHandlerService;
import top.flobby.live.im.dto.ImMsgBody;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 实现
 * @create : 2023-12-08 13:44
 **/

@Service
public class RouterHandlerServiceImpl implements IRouterHandlerService {

    @Override
    public void onReceive(ImMsgBody msgBody) {
        // 接收消息通知的userId
        Long userId = msgBody.getUserId();
        ChannelHandlerContext ctx = ChannelHandlerContextCache.get(userId);
        if (ctx != null) {
            ImMsg respMsg = ImMsg.build(ImMsgCodeEnum.IM_BUSINESS_MSG, JSON.toJSONString(msgBody));
            ctx.writeAndFlush(respMsg);
        }
    }

}
