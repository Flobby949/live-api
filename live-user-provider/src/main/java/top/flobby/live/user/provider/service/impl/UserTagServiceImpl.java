package top.flobby.live.user.provider.service.impl;

import com.alibaba.fastjson2.JSON;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.common.message.Message;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import top.flobby.live.common.utils.ConvertBeanUtils;
import top.flobby.live.framework.redis.starter.key.UserProviderCacheKeyBuilder;
import top.flobby.live.user.constants.CacheAsyncDeleteEnum;
import top.flobby.live.user.constants.UserTagsEnum;
import top.flobby.live.user.dto.UserCacheAsyncDeleteDTO;
import top.flobby.live.user.dto.UserTagDTO;
import top.flobby.live.user.provider.dao.mapper.UserTagMapper;
import top.flobby.live.user.provider.dao.po.UserTagPO;
import top.flobby.live.user.provider.service.IUserTagService;
import top.flobby.live.user.utils.CommonUtils;
import top.flobby.live.user.utils.TagInfoUtils;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
    private RedisTemplate<String, UserTagDTO> redisTemplate;
    @Resource
    private UserProviderCacheKeyBuilder keyBuilder;
    @Resource
    private MQProducer mqProducer;

    @Override
    public boolean setTag(Long userId, UserTagsEnum userTagsEnum) {
        // 1. 尝试update，成功则返回
        if (userTagMapper.setTag(userId, userTagsEnum.getFieldName(), userTagsEnum.getTag()) > 0) {
            // 从缓存中删除
            deleteUserTagFromRedis(userId);
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
        boolean updateStatus = userTagMapper.removeTag(userId, userTagsEnum.getFieldName(), userTagsEnum.getTag()) > 0;
        if (!updateStatus) {
            return false;
        }
        // 从缓存中删除
        deleteUserTagFromRedis(userId);
        return true;
    }

    @Override
    public boolean containsTag(Long userId, UserTagsEnum userTagsEnum) {
        UserTagDTO tag = queryByUserIdFormRedis(userId);
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

    /**
     * 从 Redis 中删除用户标签
     *
     * @param userId 用户 ID
     */
    private void deleteUserTagFromRedis(Long userId) {
        String key = keyBuilder.buildUserTagKey(userId);
        redisTemplate.delete(key);
        try {
            // 设置删除动作DTO，区分不同的删除动作
            UserCacheAsyncDeleteDTO userDTO = new UserCacheAsyncDeleteDTO();
            userDTO.setCode(CacheAsyncDeleteEnum.USER_TAG_DELETE.getCode());
            userDTO.setJson(JSON.toJSONString(Map.of(USER_ID, userId)));
            // 构造消息
            Message message = new Message();
            message.setTopic(CACHE_ASYNC_DELETE);
            // 延迟级别，1 代表一秒
            message.setDelayTimeLevel(1);
            message.setBody(JSON.toJSONString(userDTO).getBytes());
            mqProducer.send(message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 查询用户标签，添加到缓存
     *
     * @param userId 用户 ID
     * @return {@link UserTagDTO}
     */
    private UserTagDTO queryByUserIdFormRedis(Long userId) {
        // 从缓存中查询
        String key = keyBuilder.buildUserTagKey(userId);
        UserTagDTO userTagDTO = redisTemplate.opsForValue().get(key);
        if (!ObjectUtils.isEmpty(userTagDTO)) {
            return userTagDTO;
        }
        // 从数据库中查询
        UserTagPO tag = userTagMapper.selectById(userId);
        if (ObjectUtils.isEmpty(tag)) {
            return null;
        }
        // 存入缓存
        userTagDTO = ConvertBeanUtils.convert(tag, UserTagDTO.class);
        redisTemplate.opsForValue().set(key, userTagDTO, CommonUtils.createRandomExpireTime(), TimeUnit.SECONDS);
        return userTagDTO;
    }
}
