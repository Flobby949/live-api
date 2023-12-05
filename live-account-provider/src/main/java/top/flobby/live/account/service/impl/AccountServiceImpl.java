package top.flobby.live.account.service.impl;

import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import top.flobby.live.account.service.IAccountService;
import top.flobby.live.common.exception.BusinessException;
import top.flobby.live.common.exception.BusinessExceptionEnum;
import top.flobby.live.common.utils.JwtUtil;
import top.flobby.live.framework.redis.starter.key.AccountProviderCacheKeyBuilder;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author : Flobby
 * @program : live-api
 * @description :
 * @create : 2023-12-05 13:05
 **/

@Service
public class AccountServiceImpl implements IAccountService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private AccountProviderCacheKeyBuilder cacheKeyBuilder;

    @Override
    public String createAndSaveLoginToken(Long userId) {
        String token = JwtUtil.createToken(userId);
        // 构造key  xxx:xxx:token -> userId
        String key = cacheKeyBuilder.buildAccountTokenKey(token);
        redisTemplate.opsForValue().set(key, userId, 48, TimeUnit.HOURS);
        return token;
    }

    @Override
    public Long getUserIdByToken(String tokenKey) {
        String loginTokenKey = cacheKeyBuilder.buildAccountTokenKey(tokenKey);
        Object value = redisTemplate.opsForValue().get(loginTokenKey);
        if (ObjectUtils.isEmpty(value)) {
            throw new BusinessException(BusinessExceptionEnum.TOKEN_TIMEOUT);
        }
        Long userId = Long.parseLong(value.toString());
        Long jwtUserId = JwtUtil.getJSONObject(tokenKey).getLong("userId");
        // 如果redis中的userId和jwt中的userId不一致，说明token有异常，需要重新登录
        if (ObjectUtils.isEmpty(userId) && !Objects.equals(userId, jwtUserId)) {
            redisTemplate.delete(loginTokenKey);
            throw new BusinessException(BusinessExceptionEnum.TOKEN_ERROR);
        }
        return userId;
    }
}
