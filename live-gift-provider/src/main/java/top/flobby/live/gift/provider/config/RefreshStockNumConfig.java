package top.flobby.live.gift.provider.config;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import top.flobby.live.framework.redis.starter.key.GiftProviderCacheKeyBuilder;
import top.flobby.live.gift.interfaces.ISkuStockInfoRPC;
import top.flobby.live.gift.provider.service.IAnchorShopInfoService;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 刷新库存定时任务
 * @create : 2024-03-11 10:14
 **/

@Slf4j
@Configuration
public class RefreshStockNumConfig implements InitializingBean {
    @Resource
    private ISkuStockInfoRPC skuStockInfoRPC;
    @Resource
    private IAnchorShopInfoService anchorShopInfoService;
    @Resource
    private GiftProviderCacheKeyBuilder cacheKeyBuilder;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    private ScheduledThreadPoolExecutor scheduledPool = new ScheduledThreadPoolExecutor(1);

    @Override
    public void afterPropertiesSet() throws Exception {
        // 15秒刷新一次
        scheduledPool.scheduleAtFixedRate(new RefreshStockNumJob(), 3, 15, TimeUnit.SECONDS);
    }

    class RefreshStockNumJob implements Runnable {
        @Override
        public void run() {
            Boolean lockStatus = redisTemplate.opsForValue().setIfAbsent(cacheKeyBuilder.buildSkuSyncLock(), 1, 14, TimeUnit.SECONDS);
            if (Boolean.TRUE.equals(lockStatus)) {
                anchorShopInfoService.queryAllValidAnchorId().forEach(skuStockInfoRPC::syncStockToMysql);
                log.info("RefreshStockNumJob success");
            }
        }
    }
}
