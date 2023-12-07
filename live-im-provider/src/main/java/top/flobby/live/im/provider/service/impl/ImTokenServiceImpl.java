package top.flobby.live.im.provider.service.impl;

import cn.hutool.json.JSONObject;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import top.flobby.live.common.utils.JwtUtil;
import top.flobby.live.framework.redis.starter.key.ImProviderCacheKeyBuilder;
import top.flobby.live.im.provider.service.ImTokenService;

import java.util.concurrent.TimeUnit;

/**
 * @author : Flobby
 * @program : live-api
 * @description :
 * @create : 2023-12-07 16:55
 **/

@Service
public class ImTokenServiceImpl implements ImTokenService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private ImProviderCacheKeyBuilder cacheKeyBuilder;

    @Override
    public String createImLoginToken(Long userId, Integer appId) {
        String token = JwtUtil.createToken(userId);
        String key = cacheKeyBuilder.buildImLoginTokenKey(token);
        redisTemplate.opsForValue().set(key, appId, 5, TimeUnit.MINUTES);
        return token;
    }

    @Override
    public Long getUserIdByToken(String token) {
        JSONObject jsonObject = JwtUtil.getJSONObject(token);
        return jsonObject.getLong("userId");
    }

    @Override
    public Integer getAppIdByToken(String token) {
        String key = cacheKeyBuilder.buildImLoginTokenKey(token);
        Object appId = redisTemplate.opsForValue().get(key);
        return ObjectUtils.isEmpty(appId) ? null : (Integer) appId;
    }
}
