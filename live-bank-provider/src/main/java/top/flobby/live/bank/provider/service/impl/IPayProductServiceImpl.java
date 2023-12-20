package top.flobby.live.bank.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import top.flobby.live.bank.dto.PayProductDTO;
import top.flobby.live.bank.provider.dao.mapper.PayProductMapper;
import top.flobby.live.bank.provider.dao.po.PayProductPO;
import top.flobby.live.bank.provider.service.IPayProductService;
import top.flobby.live.common.enums.CommonStatusEnum;
import top.flobby.live.common.utils.CommonUtils;
import top.flobby.live.common.utils.ConvertBeanUtils;
import top.flobby.live.framework.redis.starter.key.BankProviderCacheKeyBuilder;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author : Flobby
 * @program : live-api
 * @description :
 * @create : 2023-12-20 08:15
 **/

@Service
public class IPayProductServiceImpl implements IPayProductService {

    @Resource
    private PayProductMapper payProductMapper;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private BankProviderCacheKeyBuilder cacheKeyBuilder;

    @Override
    public List<PayProductDTO> productList(Integer type) {
        String cacheKey = cacheKeyBuilder.buildPayProductListKey(type);
        List<PayProductDTO> cacheList = redisTemplate.opsForList().range(cacheKey, 0, -1).stream().map(x -> (PayProductDTO) x).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(cacheList)) {
            if (cacheList.get(0).getId() != null) {
                return Collections.emptyList();
            }
            return cacheList;
        }
        LambdaQueryWrapper<PayProductPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PayProductPO::getType, type);
        wrapper.eq(PayProductPO::getValidStatus, CommonStatusEnum.VALID.getCode());
        wrapper.orderByAsc(PayProductPO::getPrice);
        List<PayProductDTO> resultFromDB = ConvertBeanUtils.convertList(payProductMapper.selectList(wrapper), PayProductDTO.class);
        if (CollectionUtils.isEmpty(resultFromDB)) {
            redisTemplate.opsForList().leftPush(cacheKey, new PayProductDTO());
            redisTemplate.expire(cacheKey, 5, TimeUnit.MINUTES);
            return Collections.emptyList();
        }
        redisTemplate.opsForList().leftPushAll(cacheKey, resultFromDB.toArray());
        redisTemplate.expire(cacheKey, CommonUtils.createRandomExpireTime(), TimeUnit.SECONDS);
        return resultFromDB;
    }
}
