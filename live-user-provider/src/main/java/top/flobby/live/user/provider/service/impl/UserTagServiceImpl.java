package top.flobby.live.user.provider.service.impl;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import top.flobby.live.framework.redis.starter.key.UserProviderCacheKeyBuilder;
import top.flobby.live.user.constants.UserTagsEnum;
import top.flobby.live.user.provider.dao.mapper.UserTagMapper;
import top.flobby.live.user.provider.dao.po.UserTagPO;
import top.flobby.live.user.provider.service.IUserTagService;
import top.flobby.live.user.utils.TagInfoUtils;

import java.nio.charset.StandardCharsets;

import static top.flobby.live.user.constants.Constant.*;

/**
 * @author : Flobby
 * @program : live-api
 * @description :
 * @create : 2023-11-28 12:02
 **/

@Slf4j
@Service
public class UserTagServiceImpl implements IUserTagService {

    @Resource
    private UserTagMapper userTagMapper;
    @Resource
    private RedisTemplate<String, String> redisTemplate;
    @Resource
    private UserProviderCacheKeyBuilder keyBuilder;

    @Override
    public boolean setTag(Long userId, UserTagsEnum userTagsEnum) {
        // 1. 尝试update，成功则返回
        if (userTagMapper.setTag(userId, userTagsEnum.getFieldName(), userTagsEnum.getTag()) > 0) {
            return true;
        }

        // 2. 分布式情况下，多个线程同时进入到这里，只有一个线程能创建成功，代表加锁成功
        String setNxKey = keyBuilder.buildUserTagLockKey(userId);
        String setNxResult = redisTemplate.execute((RedisCallback<String>) connection -> {
            RedisSerializer keySerializer = redisTemplate.getKeySerializer();
            RedisSerializer valueSerializer = redisTemplate.getValueSerializer();
            return (String) connection.execute("set",
                    keySerializer.serialize(setNxKey),
                    valueSerializer.serialize("1"),
                    "NX".getBytes(StandardCharsets.UTF_8),
                    "EX".getBytes(StandardCharsets.UTF_8),
                    // 三秒后自动释放锁
                    "3".getBytes(StandardCharsets.UTF_8)
            );
        });

        // 如果获取锁失败则返回
        if (!"OK".equals(setNxResult)) {
            log.info("获取锁失败，userId：{}", userId);
            return false;
        }
        // 3. 失败则判断是否存在该用户的记录，存在代表已经存在标签，不存在则创建
        UserTagPO tag = userTagMapper.selectById(userId);
        if (!ObjectUtils.isEmpty(tag)) {
            return false;
        }
        tag = new UserTagPO();
        tag.setUserId(userId);
        userTagMapper.insert(tag);
        log.info("首次创建用户标签记录成功，userId：{}", userId);
        // 4. 创建完成后再次尝试update
        boolean updateStatus = userTagMapper.setTag(userId, userTagsEnum.getFieldName(), userTagsEnum.getTag()) > 0;
        // 释放锁
        redisTemplate.delete(setNxKey);
        return updateStatus;
    }

    @Override
    public boolean cancelTag(Long userId, UserTagsEnum userTagsEnum) {
        return userTagMapper.removeTag(userId, userTagsEnum.getFieldName(), userTagsEnum.getTag()) > 0;
    }

    @Override
    public boolean containsTag(Long userId, UserTagsEnum userTagsEnum) {
        UserTagPO tag = userTagMapper.selectById(userId);
        if (ObjectUtils.isEmpty(tag)) {
            return false;
        }
        String fieldName = userTagsEnum.getFieldName();

        Long tagInfo = switch (fieldName) {
            case TAG_INFO_01 -> tag.getTagInfo01();
            case TAG_INFO_02 -> tag.getTagInfo02();
            case TAG_INFO_03 -> tag.getTagInfo03();
            default -> throw new IllegalStateException("Unexpected value: " + fieldName);
        };
        return TagInfoUtils.isContain(tagInfo, userTagsEnum.getTag());
    }
}
