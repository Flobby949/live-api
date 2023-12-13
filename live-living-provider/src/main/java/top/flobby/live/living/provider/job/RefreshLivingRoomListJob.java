package top.flobby.live.living.provider.job;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import top.flobby.live.framework.redis.starter.key.LivingProviderCacheKeyBuilder;
import top.flobby.live.living.enums.LivingRoomTypeEnum;
import top.flobby.live.living.provider.service.ILivingRoomService;
import top.flobby.live.living.vo.LivingRoomInfoVO;

import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 获取直播间列表定时任务
 * @create : 2023-12-13 10:51
 **/

@Slf4j
@Component
public class RefreshLivingRoomListJob implements InitializingBean {
    @Resource
    private ILivingRoomService livingRoomService;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private LivingProviderCacheKeyBuilder cacheKeyBuilder;

    private ScheduledThreadPoolExecutor schedulePool = new ScheduledThreadPoolExecutor(1);

    @Override
    public void afterPropertiesSet() throws Exception {
        // 每隔5秒刷新一次
        schedulePool.scheduleWithFixedDelay(new RefreshCacheListJob(), 3000, 5000, TimeUnit.MILLISECONDS);
    }

    class RefreshCacheListJob implements Runnable {
        @Override
        public void run() {
            log.info("刷新直播间列表缓存");
            String cacheKey = cacheKeyBuilder.buildRefreshRoomListLock();
            Boolean lock = redisTemplate.opsForValue().setIfAbsent(cacheKey, 1, 5, TimeUnit.SECONDS);
            if (lock) {
                log.info("[RefreshCacheListJob] 获取锁成功，开始更新缓存");
                refreshDBToRedis(LivingRoomTypeEnum.NORMAL_ROOM.getType());
                refreshDBToRedis(LivingRoomTypeEnum.PK_ROOM.getType());
                log.info("[RefreshCacheListJob] 更新缓存结束");
            }
        }
    }

    private void refreshDBToRedis(Integer type) {
        String cacheKey = cacheKeyBuilder.buildLivingRoomListKey(type);
        List<LivingRoomInfoVO> resultList = livingRoomService.queryAllListFromDb(type);
        if (CollectionUtils.isEmpty(resultList)) {
            redisTemplate.delete(cacheKey);
            return;
        }
        String tempListName = cacheKey + "_temp";
        // 按顺序一个一个存到缓存中
        resultList.forEach(item -> {
            redisTemplate.opsForList().rightPush(tempListName, item);
        });
        // 高并发场景下，直接重命名缓存，减少阻塞
        redisTemplate.rename(tempListName, cacheKey);
        redisTemplate.delete(tempListName);
    }
}
