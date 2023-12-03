package top.flobby.live.user.provider.service.impl;

import com.alibaba.fastjson2.JSON;
import com.google.common.collect.Maps;
import jakarta.annotation.Resource;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.common.message.Message;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import top.flobby.live.common.utils.CommonUtils;
import top.flobby.live.common.utils.ConvertBeanUtils;
import top.flobby.live.framework.redis.starter.key.UserProviderCacheKeyBuilder;
import top.flobby.live.user.constants.CacheAsyncDeleteEnum;
import top.flobby.live.user.dto.UserCacheAsyncDeleteDTO;
import top.flobby.live.user.dto.UserDTO;
import top.flobby.live.user.provider.dao.mapper.UserMapper;
import top.flobby.live.user.provider.dao.po.UserPO;
import top.flobby.live.user.provider.service.IUserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static top.flobby.live.user.constants.Constant.CACHE_ASYNC_DELETE;
import static top.flobby.live.user.constants.Constant.USER_ID;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 实现类
 * @create : 2023-11-18 14:44
 **/

@Service
public class UserServiceImpl implements IUserService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private RedisTemplate<String, UserDTO> redisTemplate;
    @Resource
    private UserProviderCacheKeyBuilder userProviderCacheKeyBuilder;
    @Resource
    private MQProducer mqProducer;

    @Override
    public UserDTO getByUserId(Long userId) {
        if (userId == null) {
            return null;
        }
        String key = userProviderCacheKeyBuilder.buildUserInfoKey(userId);
        UserDTO userDTO = redisTemplate.opsForValue().get(key);
        if (userDTO != null) {
            return userDTO;
        }
        userDTO = ConvertBeanUtils.convert(userMapper.selectById(userId), UserDTO.class);
        if (userDTO != null) {
            redisTemplate.opsForValue().set(key, userDTO, CommonUtils.createRandomExpireTime(), TimeUnit.SECONDS);
        }
        return userDTO;
    }

    @Override
    public boolean updateUserInfo(UserDTO userDTO) {
        if (userDTO == null || userDTO.getUserId() == null) {
            return false;
        }
        userMapper.updateById(ConvertBeanUtils.convert(userDTO, UserPO.class));
        String key = userProviderCacheKeyBuilder.buildUserInfoKey(userDTO.getUserId());
        redisTemplate.delete(key);

        try {
            // 设置删除动作DTO，区分不同的删除动作
            UserCacheAsyncDeleteDTO userCacheAsyncDeleteDTO = new UserCacheAsyncDeleteDTO();
            userCacheAsyncDeleteDTO.setCode(CacheAsyncDeleteEnum.USER_INFO_DELETE.getCode());
            userCacheAsyncDeleteDTO.setJson(JSON.toJSONString(Map.of(USER_ID, userDTO.getUserId())));
            // 构造消息
            Message message = new Message();
            message.setTopic(CACHE_ASYNC_DELETE);
            // 延迟级别，1 代表一秒
            message.setDelayTimeLevel(1);
            message.setBody(JSON.toJSONString(userCacheAsyncDeleteDTO).getBytes());
            mqProducer.send(message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    @Override
    public boolean insertUser(UserDTO userDTO) {
        if (userDTO == null || userDTO.getUserId() == null) {
            return false;
        }
        return userMapper.insert(ConvertBeanUtils.convert(userDTO, UserPO.class)) > 0;
    }

    @Override
    public Map<Long, UserDTO> batchQueryUserInfo(List<Long> userIdList) {
        if (CollectionUtils.isEmpty(userIdList)) {
            return Maps.newHashMap();
        }
        userIdList = userIdList.stream().filter(id -> id > 10000).toList();
        if (CollectionUtils.isEmpty(userIdList)) {
            return Maps.newHashMap();
        }
        // redis 批量查询
        List<String> keyList = new ArrayList<>();
        // 批量构造 redis key
        userIdList.forEach(userId ->
                keyList.add(userProviderCacheKeyBuilder.buildUserInfoKey(userId)));
        // 取出在redis缓存中的对象，过滤空对象
        List<UserDTO> userDTOList = redisTemplate
                .opsForValue().multiGet(keyList).stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        // 全部命中
        if (!CollectionUtils.isEmpty(userDTOList) && userDTOList.size() == userIdList.size()) {
            return userDTOList.stream()
                    .collect(Collectors.toMap(UserDTO::getUserId, userDTO -> userDTO));
        }
        // 获取不在缓存中的id
        List<Long> userIdInCache = userDTOList.stream().map(UserDTO::getUserId).toList();
        List<Long> userIdNotInCache = userIdList.stream().filter(x -> !userIdInCache.contains(x)).toList();
        // 直接 userMapper.selectBatchIds(userIdList); 性能不好，union all 实现，采用多线程实现
        Map<Long, List<Long>> userIdMap = userIdNotInCache.stream()
                .collect(Collectors.groupingBy(userId -> userId % 100));
        List<UserDTO> dbQueryResult = new CopyOnWriteArrayList<>();
        userIdMap.values().parallelStream().forEach(ids ->
                dbQueryResult.addAll(ConvertBeanUtils.convertList(userMapper.selectBatchIds(ids), UserDTO.class)));
        if (!CollectionUtils.isEmpty(dbQueryResult)) {
            // 结合业务场景进行优化，是否要添加到缓存中
            Map<String, UserDTO> saveCacheMap = dbQueryResult.stream().collect(Collectors.toMap(x -> userProviderCacheKeyBuilder.buildUserInfoKey(x.getUserId()), x -> x));
            redisTemplate.opsForValue().multiSet(saveCacheMap);
            // 管道写法，批量设置过期时间
            redisTemplate.executePipelined(new SessionCallback<>() {
                @Override
                public <K, V> Object execute(RedisOperations<K, V> operations) throws DataAccessException {
                    for (String redisKey : saveCacheMap.keySet()) {
                        operations.expire((K) redisKey, CommonUtils.createRandomExpireTime(), TimeUnit.SECONDS);
                    }
                    return null;
                }
            });
            userDTOList.addAll(dbQueryResult);
        }
        return userDTOList.stream()
                .collect(Collectors.toMap(UserDTO::getUserId, userDTO -> userDTO));
    }


}
