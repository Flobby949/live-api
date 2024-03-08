package top.flobby.live.gift.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import top.flobby.live.common.enums.CommonStatusEnum;
import top.flobby.live.framework.redis.starter.key.GiftProviderCacheKeyBuilder;
import top.flobby.live.gift.provider.dao.mapper.SkuInfoMapper;
import top.flobby.live.gift.provider.dao.po.SkuInfoPO;
import top.flobby.live.gift.provider.service.ISkuInfoService;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * SKU 信息服务实现
 *
 * @author Flobby
 * @date 2024/02/26
 */

@Slf4j
@Service
public class SkuInfoServiceImpl implements ISkuInfoService {

    @Resource
    private SkuInfoMapper skuInfoMapper;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private GiftProviderCacheKeyBuilder cacheKeyBuilder;

    @Override
    public List<SkuInfoPO> queryBySkuIds(List<Long> skuIdList) {
        log.info("查询商品信息，skuIdList:{}", skuIdList);
        LambdaQueryWrapper<SkuInfoPO> qw = new LambdaQueryWrapper<>();
        qw.in(!skuIdList.isEmpty(), SkuInfoPO::getSkuId, skuIdList);
        qw.eq(SkuInfoPO::getStatus, CommonStatusEnum.VALID.getCode());
        return skuInfoMapper.selectList(qw);
    }

    @Override
    public SkuInfoPO queryBySkuId(Long skuId) {
        LambdaQueryWrapper<SkuInfoPO> qw = new LambdaQueryWrapper<>();
        qw.eq(SkuInfoPO::getSkuId, skuId);
        qw.eq(SkuInfoPO::getStatus, CommonStatusEnum.VALID.getCode());
        qw.last("limit 1");
        return skuInfoMapper.selectOne(qw);
    }

    @Override
    public SkuInfoPO queryBySkuIdFromCache(Long skuId) {
        String detailKey = cacheKeyBuilder.buildSkuDetail(skuId);
        Object skuInfoCacheObj = redisTemplate.opsForValue().get(detailKey);
        if (skuInfoCacheObj != null) {
            return (SkuInfoPO) skuInfoCacheObj;
        }
        SkuInfoPO skuInfoPO = this.queryBySkuId(skuId);
        if (skuInfoPO == null) {
            return null;
        }
        redisTemplate.opsForValue().set(detailKey, skuInfoPO, 1, TimeUnit.DAYS);
        return skuInfoPO;
    }
}