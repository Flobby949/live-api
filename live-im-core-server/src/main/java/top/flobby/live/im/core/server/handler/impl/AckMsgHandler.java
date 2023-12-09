package top.flobby.live.im.core.server.handler.impl;

import com.alibaba.fastjson2.JSON;
import io.netty.channel.ChannelHandlerContext;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import top.flobby.live.im.core.server.common.ImMsg;
import top.flobby.live.im.core.server.handler.SimplyHandler;
import top.flobby.live.im.core.server.service.IMsgAckCheckService;
import top.flobby.live.im.core.server.utils.ImContextUtils;
import top.flobby.live.im.dto.ImMsgBody;

/**
 * @author : Flobby
 * @program : live-api
 * @description :
 * @create : 2023-12-09 14:03
 **/

@Slf4j
@Component
public class AckMsgHandler implements SimplyHandler {
    @Resource
    private IMsgAckCheckService msgAckCheckService;

    @Override
    public void handler(ChannelHandlerContext ctx, ImMsg msg) {
        // 校验 msg
        Long userId = ImContextUtils.getUserId(ctx);
        Integer appId = ImContextUtils.getAppId(ctx);
        if (ObjectUtils.isEmpty(userId) || ObjectUtils.isEmpty(appId)) {
            ctx.close();
            log.error("关闭连接, imMsg:{}", msg);
            throw new IllegalArgumentException("userId为空");
        }
        // 校验 ack
        msgAckCheckService.doMsgCheck(JSON.parseObject(msg.getBody(), ImMsgBody.class));
    }
}
