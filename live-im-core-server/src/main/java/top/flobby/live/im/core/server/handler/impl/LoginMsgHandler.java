package top.flobby.live.im.core.server.handler.impl;

import com.alibaba.fastjson2.JSON;
import io.netty.channel.ChannelHandlerContext;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import top.flobby.live.common.constants.ImCoreServerTopicNameConstant;
import top.flobby.live.common.utils.JwtUtil;
import top.flobby.live.im.common.ImConstant;
import top.flobby.live.im.common.ImMsgCodeEnum;
import top.flobby.live.im.core.server.common.ChannelHandlerContextCache;
import top.flobby.live.im.core.server.common.ImMsg;
import top.flobby.live.im.core.server.dto.ImOnlineDTO;
import top.flobby.live.im.core.server.handler.SimplyHandler;
import top.flobby.live.im.core.server.utils.ImContextUtils;
import top.flobby.live.im.dto.ImMsgBody;
import top.flobby.live.im.interfaces.ImTokenRpc;

import java.util.concurrent.TimeUnit;

import static top.flobby.live.common.constants.ImCoreServerConstant.IM_BIND_IP_KEY;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 登录消息处理器
 * @create : 2023-12-07 15:33
 **/

@Slf4j
@Component
public class LoginMsgHandler implements SimplyHandler {

    @DubboReference
    private ImTokenRpc imTokenRpc;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private MQProducer mqProducer;

    @Override
    public void handler(ChannelHandlerContext ctx, ImMsg msg) {
        // 防止重复登录
        Long userIdFromCtx = ImContextUtils.getUserId(ctx);
        if (!ObjectUtils.isEmpty(userIdFromCtx)) {
            log.error("登录消息处理器, 用户已登录, msg:{}", msg);
            return;
        }
        byte[] body = msg.getBody();
        if (body == null || body.length == 0) {
            ctx.close();
            log.error("消息体为空, 关闭连接, imMsg:{}", msg);
            throw new IllegalArgumentException("消息体为空");
        }
        ImMsgBody imMsgBody = JSON.parseObject(body, ImMsgBody.class);
        Long userId = imMsgBody.getUserId();
        String token = imMsgBody.getToken();
        Integer appId = imMsgBody.getAppId();
        if (StringUtils.isEmpty(token)
                || ObjectUtils.isEmpty(userId)
                || ObjectUtils.isEmpty(appId)
                || userId < 10000
                || appId < 10000) {
            ctx.close();
            log.error("消息体参数异常, 关闭连接, imMsg:{}", imMsgBody);
            throw new IllegalArgumentException("消息体参数异常");
        }
        try {
            JwtUtil.validate(token);
            Long userIdByToken = imTokenRpc.getUserIdByToken(token);
            if (!userIdByToken.equals(userId)) {
                ctx.close();
                log.error("token验证失败, 关闭连接, imMsg:{}", msg);
                throw new IllegalArgumentException("token验证失败");
            }
        } catch (Exception e) {
            ctx.close();
            log.error("token验证失败, 关闭连接, imMsg:{}", msg);
            throw new IllegalArgumentException("token验证失败");
        }
        // TODO roomId; 校验通过，允许进行连接
        loginSuccessHandler(ctx, userId, appId, null);
    }

    /**
     * 登录成功处理
     *
     * @param ctx    CTX
     * @param userId 用户 ID
     * @param appId  应用 ID
     */
    public void loginSuccessHandler(ChannelHandlerContext ctx, Long userId, Integer appId, Long roomId) {
        // 按照 userId 保存相关信息
        ChannelHandlerContextCache.put(userId, ctx);
        // 给 ctx 设置属性，方便后续使用
        ImContextUtils.setUserId(ctx, userId);
        ImContextUtils.setAppId(ctx, appId);
        if (roomId != null) {
            ImContextUtils.setRoomId(ctx, roomId);
        }
        // 消息回写
        ImMsgBody respBody = new ImMsgBody();
        respBody.setUserId(userId);
        respBody.setAppId(appId);
        respBody.setData("true");
        ImMsg respMsg = ImMsg.build(ImMsgCodeEnum.IM_LOGIN_MSG, JSON.toJSONString(respBody));
        // 将当前服务的地址写入 redis
        // 由于需要在其他服务中获取地址，所以不能把key用之前keyBuilder的方式生成
        stringRedisTemplate.opsForValue().set(IM_BIND_IP_KEY + appId + ":" + userId,
                ChannelHandlerContextCache.getServerAddress() + "%" + userId,
                ImConstant.HEART_BEAT_TIME * 2,
                TimeUnit.SECONDS);
        log.info("【LoginMsgHandler】登录成功, userId:{}, appId:{}", userId, appId);
        ctx.writeAndFlush(respMsg);
        sendLoginMQ(userId, appId, roomId);
    }

    /**
     * 发送登录 MQ
     *
     * @param userId 用户 ID
     * @param appId  应用 ID
     */
    private void sendLoginMQ(Long userId, Integer appId, Long roomId) {
        ImOnlineDTO imOnlineDTO = new ImOnlineDTO();
        imOnlineDTO.setUserId(userId);
        imOnlineDTO.setAppId(appId);
        imOnlineDTO.setLoginTime(System.currentTimeMillis());
        imOnlineDTO.setRoomId(roomId);
        Message message = new Message();
        message.setTopic(ImCoreServerTopicNameConstant.IM_ONLINE_TOPIC);
        message.setBody(JSON.toJSONString(imOnlineDTO).getBytes());
        try {
            SendResult result = mqProducer.send(message);
            log.info("【LoginMsgHandler】发送登录消息成功, result:{}", result);
        } catch (Exception e) {
            log.error("【LoginMsgHandler】发送登录消息失败, userId:{}, appId:{}", userId, appId);
        }
    }
}
