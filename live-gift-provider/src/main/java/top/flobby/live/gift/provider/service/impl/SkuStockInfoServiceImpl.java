package top.flobby.live.gift.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import top.flobby.live.bank.constant.OrderStatusEnum;
import top.flobby.live.common.enums.CommonStatusEnum;
import top.flobby.live.framework.redis.starter.key.GiftProviderCacheKeyBuilder;
import top.flobby.live.gift.dto.RollBackStockDTO;
import top.flobby.live.gift.provider.dao.mapper.SkuStockInfoMapper;
import top.flobby.live.gift.provider.dao.po.SkuStockInfoPO;
import top.flobby.live.gift.provider.service.ISkuOrderInfoService;
import top.flobby.live.gift.provider.service.ISkuStockInfoService;
import top.flobby.live.gift.provider.service.bo.DcrStockNumBO;
import top.flobby.live.gift.vo.SkuOrderInfoVO;

import java.util.Arrays;
import java.util.List;

/**
 * @author : Flobby
 * @program : live-api
 * @description :
 * @create : 2024-03-08 14:04
 **/

@Service
public class SkuStockInfoServiceImpl implements ISkuStockInfoService {
    @Resource
    private SkuStockInfoMapper skuStockInfoMapper;
    @Resource
    private GiftProviderCacheKeyBuilder cacheKeyBuilder;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private ISkuOrderInfoService skuOrderInfoService;

    private String LUA_SCRIPT =
            "if (redis.call('exists', KEYS[1])) == 1 then " +
                    " local currentStock=redis.call('get',KEYS[1]) " +
                    "  if (tonumber(currentStock)>0 and tonumber(currentStock)-tonumber(ARGV[1])>=0)  then " +
                    "      return redis.call('decrby',KEYS[1],tonumber(ARGV[1])) " +
                    "   else return -1 end " +
                    "else " +
                    "return -1 end";

    @Override
    public DcrStockNumBO dcrStockNumBySkuId(Long skuId, Integer num) {
        SkuStockInfoPO skuStockInfoPO = this.queryBySkuId(skuId);
        DcrStockNumBO dcrStockNumBO = new DcrStockNumBO();
        if (skuStockInfoPO.getStockNum() == 0 || skuStockInfoPO.getStockNum() - num < 0) {
            dcrStockNumBO.setNoStock(true);
            dcrStockNumBO.setSuccess(false);
            return dcrStockNumBO;
        }
        dcrStockNumBO.setNoStock(false);
        boolean updateState = skuStockInfoMapper.dcrStockNumBySkuId(skuId, num, skuStockInfoPO.getVersion()) > 0;
        dcrStockNumBO.setSuccess(updateState);
        return dcrStockNumBO;
    }

    @Override
    public SkuStockInfoPO queryBySkuId(Long skuId) {
        LambdaQueryWrapper<SkuStockInfoPO> qw = new LambdaQueryWrapper<>();
        qw.eq(SkuStockInfoPO::getSkuId, skuId)
                .eq(SkuStockInfoPO::getStatus, CommonStatusEnum.VALID.getCode());
        return skuStockInfoMapper.selectOne(qw);
    }

    @Override
    public List<SkuStockInfoPO> queryBySkuIds(List<Long> skuIds) {
        LambdaQueryWrapper<SkuStockInfoPO> qw = new LambdaQueryWrapper<>();
        qw.in(!skuIds.isEmpty(), SkuStockInfoPO::getSkuId, skuIds)
                .eq(SkuStockInfoPO::getStatus, CommonStatusEnum.VALID.getCode());
        return skuStockInfoMapper.selectList(qw);
    }

    @Override
    public boolean updateStockNum(Long skuId, Integer stockNum) {
        SkuStockInfoPO po = new SkuStockInfoPO();
        po.setStockNum(stockNum);
        LambdaUpdateWrapper<SkuStockInfoPO> uw = new LambdaUpdateWrapper<>();
        uw.eq(SkuStockInfoPO::getSkuId, skuId)
                .eq(SkuStockInfoPO::getStatus, CommonStatusEnum.VALID.getCode());
        return skuStockInfoMapper.update(po, uw) > 0;
    }

    @Override
    public boolean decrStockNumBySkuIdInLua(Long skuId, Integer num) {
        // 1. 根据skuId查询库存
        // 2. 判断库存，库存 >= num
        // 3. 扣减库存
        // 但是这种方式存在并发问题，直接使用redis命令会出现多元操作，可能会导致超卖，所以需要使用lua方案
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(LUA_SCRIPT, Long.class);
        String cacheKey = cacheKeyBuilder.buildSkuStock(skuId);
        Long result = redisTemplate.execute(redisScript, List.of(cacheKey), num);
        return result >= 0;
    }

    @Override
    public void stockRollBackHandler(RollBackStockDTO dto) {
        SkuOrderInfoVO orderInfo = skuOrderInfoService.queryByOrderId(dto.getOrderId());
        if (orderInfo == null || orderInfo.getStatus().equals(OrderStatusEnum.PAY_SUCCESS.getCode().intValue())) {
            return;
        }
        // 状态回滚
        skuOrderInfoService.updateStatus(dto.getOrderId(), OrderStatusEnum.CANCEL.getCode().intValue());
        // 库存回滚
        // 当前业务场景为了简便，每件商品只能购买一件，所以库存回滚的时候，只需要加回去即可
        String skuIdStr = orderInfo.getSkuIdList();
        Arrays.asList(skuIdStr.split(","))
                .stream().map(Long::parseLong)
                .forEach(skuId -> {
                    String cacheKey = cacheKeyBuilder.buildSkuStock(skuId);
                    redisTemplate.opsForValue().increment(cacheKey, 1);
                });
    }
}
