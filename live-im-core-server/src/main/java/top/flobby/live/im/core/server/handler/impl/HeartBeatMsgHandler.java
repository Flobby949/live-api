package top.flobby.live.im.core.server.handler.impl;

import com.alibaba.fastjson2.JSON;
import io.netty.channel.ChannelHandlerContext;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import top.flobby.live.framework.redis.starter.key.ImCoreServerCacheKeyBuilder;
import top.flobby.live.im.common.ImConstant;
import top.flobby.live.im.common.ImMsgCodeEnum;
import top.flobby.live.im.core.server.common.ImMsg;
import top.flobby.live.im.core.server.constant.ImCoreServerConstant;
import top.flobby.live.im.core.server.handler.SimplyHandler;
import top.flobby.live.im.core.server.utils.ImContextUtils;
import top.flobby.live.im.dto.ImMsgBody;

import java.util.concurrent.TimeUnit;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 心跳消息处理器
 * @create : 2023-12-07 15:33
 **/

@Slf4j
@Component
public class HeartBeatMsgHandler implements SimplyHandler {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private ImCoreServerCacheKeyBuilder cacheKeyBuilder;
    @Resource
    private StringRedisTemplate stringRedisTemplate;


    @Override
    public void handler(ChannelHandlerContext ctx, ImMsg msg) {
        // 心跳包基本校验
        Long userId = ImContextUtils.getUserId(ctx);
        Integer appId = ImContextUtils.getAppId(ctx);
        if (ObjectUtils.isEmpty(userId) || ObjectUtils.isEmpty(appId)) {
            log.error("心跳消息处理器, 用户未登录, msg:{}", msg);
            ctx.close();
            return;
        }
        // 心跳包record记录，redis存储记录
        // 基于UserId 进行取模，防止出现大key，live-im-core-server:heartBeat:999:zSet
        String key = cacheKeyBuilder.buildImOnlineZSetKey(userId, appId);
        // zSet 集合存储心跳记录，key为userId，value为心跳时间
        recordOnlineTime(userId, key);
        // 删除过期记录
        removeExpireRecord(key);
        // 过期时间设置为5分钟
        redisTemplate.expire(key, 5, TimeUnit.MINUTES);
        // 延长用户ID绑定的IP地址过期时间，延长两个心跳包的时间
        stringRedisTemplate.expire(ImCoreServerConstant.IM_BIND_IP_KEY + appId + ":" + userId,
                ImConstant.HEART_BEAT_TIME * 2,
                TimeUnit.SECONDS);
        // 返回心跳包
        ImMsgBody msgBody = ImMsgBody.builder()
                .userId(userId)
                .appId(appId)
                .data("true")
                .build();
        ImMsg respMsg = ImMsg.build(ImMsgCodeEnum.IM_HEARTBEAT_MSG, JSON.toJSONString(msgBody));
        log.info("[ HeartBeatMsgHandler ]  返回心跳消息, msg:{}", respMsg);
        ctx.writeAndFlush(respMsg);
    }

    /**
     * 删除过期记录
     * 两次心跳包发送间隔都没有更新记录，说明用户已经下线，删除记录
     *
     * @param key key
     */
    private void removeExpireRecord(String key) {
        // 从 ZSet 中移除超时的记录
        redisTemplate.opsForZSet().removeRangeByScore(key, 0, System.currentTimeMillis() - ImConstant.HEART_BEAT_TIME * 1000 * 2);
    }

    /**
     * 记录用户最近一次心跳时间到 ZSet上
     *
     * @param userId 用户 ID
     * @param key    钥匙
     */
    private void recordOnlineTime(Long userId, String key) {
        redisTemplate.opsForZSet().add(key, userId, System.currentTimeMillis());
    }
}
