package top.flobby.live.gift.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import top.flobby.live.common.utils.CommonUtils;
import top.flobby.live.common.utils.ConvertBeanUtils;
import top.flobby.live.framework.redis.starter.key.GiftProviderCacheKeyBuilder;
import top.flobby.live.gift.dto.SkuOrderInfoDTO;
import top.flobby.live.gift.provider.dao.mapper.SkuOrderInfoMapper;
import top.flobby.live.gift.provider.dao.po.SkuOrderInfoPO;
import top.flobby.live.gift.provider.service.ISkuOrderInfoService;
import top.flobby.live.gift.vo.SkuOrderInfoVO;

import java.util.concurrent.TimeUnit;

/**
 * @author : Flobby
 * @program : live-api
 * @description :
 * @create : 2024-03-11 11:56
 **/

@Service
public class SkuOrderInfoServiceImpl implements ISkuOrderInfoService {
    @Resource
    private SkuOrderInfoMapper skuOrderInfoMapper;
    @Resource
    private GiftProviderCacheKeyBuilder cacheKeyBuilder;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public SkuOrderInfoVO queryLastByUserIdAndRoomId(Long userId, Integer roomId) {
        String cacheKey = cacheKeyBuilder.buildUserSkuOrder(userId, roomId);
        Object cacheObj = redisTemplate.opsForValue().get(cacheKey);
        if (cacheObj != null) {
            return ConvertBeanUtils.convert(cacheObj, SkuOrderInfoVO.class);
        }
        LambdaQueryWrapper<SkuOrderInfoPO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SkuOrderInfoPO::getUserId, userId);
        queryWrapper.eq(SkuOrderInfoPO::getRoomId, roomId);
        queryWrapper.orderByDesc(SkuOrderInfoPO::getCreateTime);
        queryWrapper.last("limit 1");
        SkuOrderInfoPO lastOne = skuOrderInfoMapper.selectOne(queryWrapper);
        SkuOrderInfoVO resultDto = ConvertBeanUtils.convert(lastOne, SkuOrderInfoVO.class);
        if (lastOne != null) {
            redisTemplate.opsForValue().set(cacheKey, resultDto, CommonUtils.createRandomExpireTime(), TimeUnit.SECONDS);
        }
        return resultDto;
    }

    @Override
    public SkuOrderInfoVO queryByOrderId(Integer orderId) {
        String cacheKey = cacheKeyBuilder.buildSkuOrderInfo(orderId);
        Object cacheObj = redisTemplate.opsForValue().get(cacheKey);
        if (cacheObj != null) {
            return ConvertBeanUtils.convert(cacheObj, SkuOrderInfoVO.class);
        }
        SkuOrderInfoVO infoVo = ConvertBeanUtils.convert(skuOrderInfoMapper.selectById(orderId), SkuOrderInfoVO.class);
        if (infoVo != null) {
            redisTemplate.opsForValue().set(cacheKey, infoVo, CommonUtils.createRandomExpireTime(), TimeUnit.SECONDS);
        }
        return infoVo;
    }

    @Override
    public SkuOrderInfoPO insertOne(SkuOrderInfoDTO skuOrderInfoDTO) {
        String skuIds = StringUtils.join(skuOrderInfoDTO.getSkuIds(), ",");
        SkuOrderInfoPO po = ConvertBeanUtils.convert(skuOrderInfoDTO, SkuOrderInfoPO.class);
        po.setSkuIdList(skuIds);
        skuOrderInfoMapper.insert(po);
        return po;
    }

    @Override
    public boolean updateStatus(Integer orderId, Integer status) {
        SkuOrderInfoPO skuOrderInfoPO = skuOrderInfoMapper.selectById(orderId);
        skuOrderInfoPO.setStatus(status);
        boolean flag = skuOrderInfoMapper.updateById(skuOrderInfoPO) > 0;
        if (flag) {
            String cacheKey = cacheKeyBuilder.buildUserSkuOrder(skuOrderInfoPO.getUserId(), skuOrderInfoPO.getRoomId());
            redisTemplate.delete(cacheKey);
        }
        return flag;
    }
}
