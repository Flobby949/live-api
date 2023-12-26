package top.flobby.live.gift.provider.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import top.flobby.live.common.constants.GiftProviderTopicNamesConstant;
import top.flobby.live.common.enums.CommonStatusEnum;
import top.flobby.live.common.utils.CommonUtils;
import top.flobby.live.common.utils.ConvertBeanUtils;
import top.flobby.live.framework.redis.starter.key.GiftProviderCacheKeyBuilder;
import top.flobby.live.gift.dto.GiftConfigDTO;
import top.flobby.live.gift.provider.dao.mapper.GiftMapper;
import top.flobby.live.gift.provider.dao.po.GiftPO;
import top.flobby.live.gift.provider.service.IGiftConfigService;
import top.flobby.live.gift.provider.service.bo.GiftCacheRemoveBO;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author : Flobby
 * @program : live-api
 * @description :
 * @create : 2023-12-17 13:01
 **/

@Slf4j
@Service
public class GiftConfigServiceImpl implements IGiftConfigService {

    @Resource
    private GiftMapper giftMapper;
    @Resource
    private MQProducer mqProducer;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private GiftProviderCacheKeyBuilder cacheKeyBuilder;

    @Override
    public GiftConfigDTO getGiftById(Integer giftId) {
        String cacheKey = cacheKeyBuilder.buildGiftObjKey(giftId);
        // 使用缓存去抵挡对db层的访问压力
        GiftConfigDTO giftConfigDTO = (GiftConfigDTO) redisTemplate.opsForValue().get(cacheKey);
        if (giftConfigDTO != null) {
            if (giftConfigDTO.getGiftId() != null) {
                // 延长缓存时间
                redisTemplate.expire(cacheKey, CommonUtils.createRandomExpireTime(), TimeUnit.SECONDS);
                return giftConfigDTO;
            }
            // 空值缓存
            return null;
        }
        // 缓存中没有，从数据库中获取
        return getGiftByIdFromDB(giftId, cacheKey);
    }

    /**
     * 通过ID从数据库获取礼物
     *
     * @param giftId   礼品 ID
     * @param cacheKey 缓存键
     * @return {@link GiftConfigDTO}
     */
    private GiftConfigDTO getGiftByIdFromDB(Integer giftId, String cacheKey) {
        LambdaQueryWrapper<GiftPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GiftPO::getGiftId, giftId);
        wrapper.eq(GiftPO::getStatus, CommonStatusEnum.VALID.getCode());
        GiftPO giftPO = giftMapper.selectOne(wrapper);
        // 检索出来的数据，要重新存入cache中
        if (giftPO != null) {
            // 如果存在该对象，则缓存到redis中
            GiftConfigDTO result = ConvertBeanUtils.convert(giftPO, GiftConfigDTO.class);
            redisTemplate.opsForValue().set(cacheKey, result, CommonUtils.createRandomExpireTime(), TimeUnit.SECONDS);
            return result;
        }
        // 避免二次请求对db的访问压力
        // 假设说 我们是一个非常大的并发场景，大量的请求落入到getByGiftId方法中，假设我们的后台下架了某个礼物
        redisTemplate.opsForValue().set(cacheKey, new GiftConfigDTO(), 5, TimeUnit.MINUTES);
        return null;
    }

    @Override
    public List<GiftConfigDTO> queryGiftList() {
        String cacheKey = cacheKeyBuilder.buildGiftListKey();
        // 礼物的列表数据不会特别多，直接进行list的全量便利
        List<GiftConfigDTO> cacheList = redisTemplate.opsForList().range(cacheKey, 0, 100).stream()
                .map(x -> (GiftConfigDTO) x).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(cacheList)) {
            // 不是空list缓存
            if (cacheList.get(0).getGiftId() != null) {
                redisTemplate.expire(cacheKey, 60, TimeUnit.MINUTES);
                return cacheList;
            }
            return Collections.emptyList();
        }
        // 缓存中没有，从数据库中获取
        return queryGiftListFromDB(cacheKey);
    }

    /**
     * 从数据库查询礼品清单
     *
     * @param cacheKey 缓存键
     * @return {@link List}<{@link GiftConfigDTO}>
     */
    public List<GiftConfigDTO> queryGiftListFromDB(String cacheKey) {
        LambdaQueryWrapper<GiftPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GiftPO::getStatus, CommonStatusEnum.VALID.getCode());
        List<GiftPO> giftPOList = giftMapper.selectList(wrapper);
        if (!CollectionUtils.isEmpty(giftPOList)) {
            // 如果存在该对象，则缓存到redis中
            List<GiftConfigDTO> resultList = ConvertBeanUtils.convertList(giftPOList, GiftConfigDTO.class);
            Boolean trySetToRedis = redisTemplate.opsForValue().setIfAbsent(cacheKeyBuilder.buildGiftConsumeLockKey(), 1, 3, TimeUnit.SECONDS);
            if (Boolean.TRUE.equals(trySetToRedis)) {
                // 如果成功获取到锁
                redisTemplate.opsForList().leftPushAll(cacheKey, resultList.toArray());
                redisTemplate.expire(cacheKey, CommonUtils.createRandomExpireTime(), TimeUnit.SECONDS);
            }
            return resultList;
        }
        // 存入一个空的list进入redis中
        redisTemplate.opsForList().leftPush(cacheKey, new GiftConfigDTO());
        redisTemplate.expire(cacheKey, 5, TimeUnit.MINUTES);
        return Collections.emptyList();
    }


    @Override
    public void insertOne(GiftConfigDTO giftConfigDTO) {
        GiftPO giftPO = ConvertBeanUtils.convert(giftConfigDTO, GiftPO.class);
        giftPO.setStatus(CommonStatusEnum.VALID.getCode());
        giftPO.setCreateTime(new Date());
        giftPO.setUpdateTime(new Date());
        giftMapper.insert(giftPO);
        deleteCacheDouble(null);
    }

    @Override
    public void updateOne(GiftConfigDTO giftConfigDTO) {
        GiftPO giftPO = ConvertBeanUtils.convert(giftConfigDTO, GiftPO.class);
        giftMapper.updateById(giftPO);
        deleteCacheDouble(giftPO.getGiftId());
    }

    /**
     * 缓存延迟双删
     *
     * @param giftId 礼品 ID
     */
    private void deleteCacheDouble(Integer giftId) {
        GiftCacheRemoveBO giftCacheRemoveBO = new GiftCacheRemoveBO();
        // 第一次删除
        redisTemplate.delete(cacheKeyBuilder.buildGiftListKey());
        if (giftId != null) {
            // giftId不为空，代表是更新操作，需要删除单个礼物的缓存
            redisTemplate.delete(cacheKeyBuilder.buildGiftObjKey(giftId));
            giftCacheRemoveBO.setGiftId(giftId);
        }
        // MQ双删
        giftCacheRemoveBO.setRemoveListCache(true);
        Message message = new Message();
        message.setTopic(GiftProviderTopicNamesConstant.REMOVE_GIFT_CACHE);
        message.setBody(JSON.toJSONBytes(giftCacheRemoveBO));
        // 1秒之后延迟消费
        message.setDelayTimeLevel(1);
        try {
            SendResult sendResult = mqProducer.send(message);
            log.info("[GiftServiceImpl] sendResult is {}", sendResult);
        } catch (Exception e) {
            log.info("[GiftServiceImpl] mq send error: }", e);
        }
    }
}
