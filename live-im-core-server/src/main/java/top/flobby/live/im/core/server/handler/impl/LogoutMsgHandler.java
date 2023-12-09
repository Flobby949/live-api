package top.flobby.live.im.core.server.handler.impl;

import com.alibaba.fastjson2.JSON;
import io.netty.channel.ChannelHandlerContext;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import top.flobby.live.im.common.AppIdEnum;
import top.flobby.live.im.common.ImMsgCodeEnum;
import top.flobby.live.im.core.server.common.ChannelHandlerContextCache;
import top.flobby.live.im.core.server.common.ImMsg;
import top.flobby.live.im.core.server.handler.SimplyHandler;
import top.flobby.live.im.core.server.utils.ImContextUtils;
import top.flobby.live.im.dto.ImMsgBody;

import static top.flobby.live.common.constants.ImCoreServerConstant.IM_BIND_IP_KEY;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 登出消息处理器
 * @create : 2023-12-07 15:33
 **/

@Slf4j
@Component
public class LogoutMsgHandler implements SimplyHandler {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void handler(ChannelHandlerContext ctx, ImMsg msg) {
        Long userId = ImContextUtils.getUserId(ctx);
        Integer appId = ImContextUtils.getAppId(ctx);
        if (ObjectUtils.isEmpty(userId) || ObjectUtils.isEmpty(appId)) {
            ctx.close();
            log.error("userId为空, 关闭连接, imMsg:{}", msg);
            throw new IllegalArgumentException("userId为空");
        }
        // 回写 IM 消息
        ImMsgBody respBody = new ImMsgBody();
        respBody.setUserId(userId);
        respBody.setAppId(AppIdEnum.LIVE_BIZ_ID.getCode());
        respBody.setData("true");
        ImMsg respMsg = ImMsg.build(ImMsgCodeEnum.IM_LOGOUT_MSG, JSON.toJSONString(respBody));
        ctx.writeAndFlush(respMsg);
        // 理想情况下，客户端断线时会发送断线消息包，但是防止客户端异常断线，所以从ctx中获取userId
        ChannelHandlerContextCache.remove(userId);
        stringRedisTemplate.delete(IM_BIND_IP_KEY + appId + ":" + userId);
        ImContextUtils.removeUserId(ctx);
        ImContextUtils.removeAppId(ctx);
        ctx.close();
    }
}
