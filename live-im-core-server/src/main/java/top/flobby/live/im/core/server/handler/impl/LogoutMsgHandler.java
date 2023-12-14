package top.flobby.live.im.core.server.handler.impl;

import com.alibaba.fastjson2.JSON;
import io.netty.channel.ChannelHandlerContext;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import top.flobby.live.common.constants.ImCoreServerTopicNameConstant;
import top.flobby.live.im.common.ImMsgCodeEnum;
import top.flobby.live.im.core.server.common.ChannelHandlerContextCache;
import top.flobby.live.im.core.server.common.ImMsg;
import top.flobby.live.im.core.server.dto.ImOfflineDTO;
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
    @Resource
    private MQProducer mqProducer;

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
        logoutHandler(ctx, userId, appId);
        logoutMsgNotice(ctx, userId, appId);
    }

    /**
     * 登出的时候，发送确认信号，这个是正常网络断开才会发送，异常断线则不发送
     *
     * @param ctx    CTX
     * @param userId 用户 ID
     * @param appId  应用 ID
     */
    private void logoutMsgNotice(ChannelHandlerContext ctx, Long userId, Integer appId) {
        ImMsgBody respBody = new ImMsgBody();
        respBody.setAppId(appId);
        respBody.setUserId(userId);
        respBody.setData("true");
        ImMsg respMsg = ImMsg.build(ImMsgCodeEnum.IM_LOGOUT_MSG.getCode(), JSON.toJSONString(respBody));
        ctx.writeAndFlush(respMsg);
        ctx.close();
    }

    private void logoutHandler(ChannelHandlerContext ctx, Long userId, Integer appId) {
        log.info("[LogoutMsgHandler] logout success,userId is {},appId is {}", userId, appId);
        // 理想情况下，客户端断线时会发送断线消息包，但是防止客户端异常断线，所以从ctx中获取userId
        ChannelHandlerContextCache.remove(userId);
        stringRedisTemplate.delete(IM_BIND_IP_KEY + appId + ":" + userId);
        ImContextUtils.removeUserId(ctx);
        ImContextUtils.removeAppId(ctx);
        sendLogoutMQMsg(ctx, userId, appId);
    }

    /**
     * 发送注销 MSG
     *
     * @param userId 用户 ID
     * @param appId  应用 ID
     */
    private void sendLogoutMQMsg(ChannelHandlerContext ctx, Long userId, Integer appId) {
        ImOfflineDTO imOfflineDTO = new ImOfflineDTO();
        imOfflineDTO.setUserId(userId);
        imOfflineDTO.setRoomId(ImContextUtils.getRoomId(ctx));
        imOfflineDTO.setAppId(appId);
        imOfflineDTO.setLogoutTime(System.currentTimeMillis());
        Message message = new Message(ImCoreServerTopicNameConstant.IM_OFFLINE_TOPIC, JSON.toJSONString(imOfflineDTO).getBytes());
        try {
            SendResult sendResult = mqProducer.send(message);
            log.info("[LogoutMsgHandler]发送IM下线消息成功, sendResult:{}", sendResult);
        } catch (Exception e) {
            log.error("[LogoutMsgHandler]发送IM下线消息失败, ", e);
        }
    }
}
