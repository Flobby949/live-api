package top.flobby.live.im.provider.service.impl;

import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import top.flobby.live.im.provider.service.ImOnlineService;

import static top.flobby.live.common.constants.ImCoreServerConstant.IM_BIND_IP_KEY;

/**
 * @author : Flobby
 * @program : live-api
 * @description : impl
 * @create : 2023-12-09 13:42
 **/

@Service
public class ImOnlineServiceImpl implements ImOnlineService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public boolean isOnline(Long userId, Integer appId) {
        /*
            1. ChannelHandlerContextCache中保存了所有的用户连接，但是通过这个方法判断用户是否在线调用链路太长
            2. 通过redis中绑定的用户ID和应用ID来判断用户是否在线，但是在异常断线情况下，无法及时清除redis中的数据
        */
        return Boolean.TRUE.equals(redisTemplate.hasKey(IM_BIND_IP_KEY + appId + ":" + userId));
    }
}
