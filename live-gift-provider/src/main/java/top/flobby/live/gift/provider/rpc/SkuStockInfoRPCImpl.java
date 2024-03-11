package top.flobby.live.gift.provider.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import top.flobby.live.framework.redis.starter.key.GiftProviderCacheKeyBuilder;
import top.flobby.live.gift.interfaces.ISkuStockInfoRPC;
import top.flobby.live.gift.provider.dao.po.SkuStockInfoPO;
import top.flobby.live.gift.provider.service.IAnchorShopInfoService;
import top.flobby.live.gift.provider.service.ISkuStockInfoService;
import top.flobby.live.gift.provider.service.bo.DcrStockNumBO;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author : Flobby
 * @program : live-api
 * @description :
 * @create : 2024-03-08 14:01
 **/

@DubboService
public class SkuStockInfoRPCImpl implements ISkuStockInfoRPC {
    @Resource
    private ISkuStockInfoService stockInfoService;
    @Resource
    private IAnchorShopInfoService anchorShopInfoService;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private GiftProviderCacheKeyBuilder cacheKeyBuilder;

    private final int MAX_TRY_TIMES = 5;

    @Override
    public boolean dcrStockNumBySkuId(Long skuId, Integer num) {
        for (int i = 0; i < MAX_TRY_TIMES; i++) {
            DcrStockNumBO dcrStockNumBO = stockInfoService.dcrStockNumBySkuId(skuId, num);
            if (dcrStockNumBO.isNoStock()) {
                return false;
            } else if (dcrStockNumBO.isSuccess()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean decrStockNumBySkuId(Long skuId, Integer num) {
        return stockInfoService.decrStockNumBySkuIdInLua(skuId, num);
    }

    @Override
    public boolean prepareStockToRedis(Long anchorId) {
        List<Long> skuIds = anchorShopInfoService.querySkuIdByAnchorId(anchorId);
        List<SkuStockInfoPO> skuStockInfoList = stockInfoService.queryBySkuIds(skuIds);
        // 如果主播商品特別少，也可以用for简单实现，更合适的做法是使用multiSet
        Map<String, Integer> collectMap =
                skuStockInfoList.stream()
                        .collect(Collectors.toMap(infoItem -> cacheKeyBuilder.buildSkuStock(infoItem.getSkuId()), x -> x.getStockNum()));
        redisTemplate.opsForValue().multiSet(collectMap);
        redisTemplate.executePipelined(new SessionCallback<>() {
            @Override
            public <K, V> Object execute(RedisOperations<K, V> operations) throws DataAccessException {
                for (String redisKey : collectMap.keySet()) {
                    operations.expire((K) redisKey, 1, TimeUnit.DAYS);
                }
                return null;
            }
        });
        return true;
    }

    @Override
    public Integer queryStockNumBySkuId(Long skuId) {
        String cacheKey = cacheKeyBuilder.buildSkuStock(skuId);
        Object stockNum = redisTemplate.opsForValue().get(cacheKey);
        if (stockNum != null) {
            return (Integer) stockNum;
        }
        return null;
    }

    @Override
    public boolean syncStockToMysql(Long anchorId) {
        List<Long> skuIds = anchorShopInfoService.querySkuIdByAnchorId(anchorId);
        skuIds.forEach(skuId -> {
            Integer stockNum = queryStockNumBySkuId(skuId);
            if (stockNum != null) {
                stockInfoService.updateStockNum(skuId, stockNum);
            }
        });
        return true;
    }
}
