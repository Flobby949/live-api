package top.flobby.live.id.generate.service.impl;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import top.flobby.live.id.generate.dao.mapper.IdGenerateMapper;
import top.flobby.live.id.generate.dao.po.IdGenerateConfigPO;
import top.flobby.live.id.generate.service.IdGenerateService;
import top.flobby.live.id.generate.service.bo.LocalSeqBO;
import top.flobby.live.id.generate.service.bo.LocalUnSeqBO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author : Flobby
 * @program : live-api
 * @description :
 * @create : 2023-11-20 11:42
 **/

@Slf4j
@Service
public class IdGenerateServiceImpl implements IdGenerateService, InitializingBean {

    private static final Map<Integer, LocalSeqBO> localSeqBOMap = new ConcurrentHashMap<>();
    private static final Map<Integer, LocalUnSeqBO> localUnSeqBOMap = new ConcurrentHashMap<>();
    private static final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(4,
            8,
            3,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(50),
            r -> {
                Thread thread = new Thread(r);
                thread.setName("id-generate-thread-" + ThreadLocalRandom.current().nextInt(1000));
                return thread;
            });
    private static final float UPDATE_RATE = 0.75f;
    private static Map<Integer, Semaphore> semaphoreMap = new ConcurrentHashMap<>();

    @Resource
    private IdGenerateMapper idGenerateMapper;

    @Override
    public Long getSeqId(Integer id) {
        if (id == null) {
            log.error("[getSeqId] is error, id is null");
            return null;
        }
        LocalSeqBO localSeqBO = localSeqBOMap.get(id);
        if (localSeqBO == null) {
            log.error("[getSeqId] is error, localSeqBO is null");
            return null;
        }
        // 本地id段的提前刷新
        this.refreshLocalSeqId(localSeqBO);
        long returnId = localSeqBO.getCurrentNum().getAndIncrement();
        if (returnId > localSeqBO.getNextThreshold()) {
            log.error("[getSeqId] is error, id段已经上限");
            return null;
        }
        return returnId;
    }

    @Override
    public Long getUnSeqId(Integer id) {
        if (id == null) {
            log.error("[getUnSeqId] is error, id is null");
            return null;
        }
        LocalUnSeqBO unSeqBO = localUnSeqBOMap.get(id);
        if (unSeqBO == null) {
            log.error("[getUnSeqId] is error, unSeqBO is null");
            return null;
        }
        Long returnId = unSeqBO.getIdQueue().poll();
        if (returnId == null) {
            log.error("[getUnSeqId] is error, id 为空");
            return null;
        }
        // 本地id段的提前刷新
        refreshLocalUnSeqId(unSeqBO);
        return returnId;
    }

    /**
     * 刷新本地 无序 ID
     *
     * @param unSeqBO bo
     */
    private void refreshLocalUnSeqId(LocalUnSeqBO unSeqBO) {
        long begin = unSeqBO.getCurrentStart();
        long end = unSeqBO.getNextThreshold();
        // 剩余长度
        long remainSize = unSeqBO.getIdQueue().size();
        // 如果剩余的长度不足全部长度的25%，则进行刷新
        if ((end - begin) * 0.25 > remainSize) {
            Semaphore semaphore = semaphoreMap.get(unSeqBO.getId());
            if (semaphore == null) {
                log.error("[refreshLocalSeqId] id is error, semaphore is null");
                return;
            }
            // 判断是否有线程正在更新抢占
            boolean acquire = semaphore.tryAcquire();
            if (acquire) {
                threadPoolExecutor.execute(() -> {
                    try {
                        log.info("{},[refreshLocalUnSeqId] 进行更新", Thread.currentThread().getName());
                        IdGenerateConfigPO idGeneratePO = idGenerateMapper.selectById(unSeqBO.getId());
                        tryUpdateMySqlRecord(idGeneratePO);
                        log.info("[refreshLocalUnSeqId] 无序id段更新成功");
                    } catch (Exception e) {
                        log.error("[refreshLocalUnSeqId] 更新失败" + e);
                    } finally {
                        // 释放信号量
                        semaphoreMap.get(unSeqBO.getId()).release();
                    }

                });
            }
        }
    }

    /**
     * 刷新本地 有序id段
     *
     * @param localSeqBO bo
     */
    private void refreshLocalSeqId(LocalSeqBO localSeqBO) {
        // 当前id段的长度
        long step = localSeqBO.getNextThreshold() - localSeqBO.getCurrentStart();
        // 当前已经使用的id数量 大于 id段长度的75%时，进行更新
        if (localSeqBO.getCurrentNum().get() - localSeqBO.getCurrentStart() > step * UPDATE_RATE) {
            Semaphore semaphore = semaphoreMap.get(localSeqBO.getId());
            if (semaphore == null) {
                log.error("[refreshLocalSeqId] id is error, semaphore is null");
                return;
            }
            // 判断是否有线程正在更新抢占
            boolean acquire = semaphore.tryAcquire();
            if (acquire) {
                log.info("[refreshLocalSeqId] id段已经使用75%，尝试进行更新");
                // 异步更新mysql表中的id段，占用对应的id段
                threadPoolExecutor.execute(() -> {
                    try {
                        log.info("{},[refreshLocalSeqId] 进行更新", Thread.currentThread().getName());
                        IdGenerateConfigPO idGeneratePO = idGenerateMapper.selectById(localSeqBO.getId());
                        tryUpdateMySqlRecord(idGeneratePO);
                        log.info("[refreshLocalSeqId] id段更新成功");
                    } catch (Exception e) {
                        log.error("[refreshLocalSeqId] 更新失败" + e);
                    } finally {
                        // 释放信号量
                        semaphoreMap.get(localSeqBO.getId()).release();
                    }
                });
            }
        }
    }

    /**
     * 更新mysql表中的id段，占用对应的id段
     * 同步执行，很多网络IO，性能较差
     *
     * @param idGeneratePO po
     */
    private void tryUpdateMySqlRecord(IdGenerateConfigPO idGeneratePO) {
        // 更新mysql表中的id段，占用对应的id段
        int updateResult = idGenerateMapper.updateNewIdAndVersion(idGeneratePO.getId(), idGeneratePO.getVersion());
        // 更新成功，说明当前线程抢占成功
        if (updateResult > 0) {
            localIdBoHandler(idGeneratePO);
            return;
        }
        // 更新失败，说明有其他线程已经更新过了，进行重试
        for (int i = 0; i < 3; i++) {
            log.info("[tryUpdateMySqlRecord] update fail, 更新重试：{} ", i + 1);
            idGeneratePO = idGenerateMapper.selectById(idGeneratePO.getId());
            updateResult = idGenerateMapper.updateNewIdAndVersion(idGeneratePO.getId(), idGeneratePO.getVersion());
            if (updateResult > 0) {
                localIdBoHandler(idGeneratePO);
                return;
            }
        }
        throw new RuntimeException("[tryUpdateMySqlRecord] update fail,表id段抢占失败");
    }

    /**
     * 处理如何将id段放入本地缓存并初始化
     *
     * @param idGeneratePO po
     */
    private void localIdBoHandler(IdGenerateConfigPO idGeneratePO) {
        // 提取当前值
        long step = idGeneratePO.getStep();
        long nextThreshold = idGeneratePO.getNextThreshold();
        // 判断有序id段还是无序id段
        if (Boolean.TRUE.equals(idGeneratePO.getIsSeq())) {
            LocalSeqBO localSeqBO = new LocalSeqBO();
            localSeqBO.setId(idGeneratePO.getId());
            localSeqBO.setCurrentNum(new AtomicLong(nextThreshold));
            localSeqBO.setCurrentStart(nextThreshold);
            localSeqBO.setNextThreshold(nextThreshold + step);
            // 将当前线程抢占的id段放入本地缓存
            localSeqBOMap.put(localSeqBO.getId(), localSeqBO);
        } else {
            LocalUnSeqBO unSeqBO = new LocalUnSeqBO();
            unSeqBO.setId(idGeneratePO.getId());
            unSeqBO.setCurrentStart(nextThreshold);
            unSeqBO.setNextThreshold(nextThreshold + step);
            List<Long> idList = new ArrayList<>();
            for (long i = unSeqBO.getCurrentStart(); i < unSeqBO.getNextThreshold(); i++) {
                idList.add(i);
            }
            // 打乱id段的顺序，放入队列
            Collections.shuffle(idList);
            ConcurrentLinkedQueue<Long> idQueue = new ConcurrentLinkedQueue<>();
            idQueue.addAll(idList);
            unSeqBO.setIdQueue(idQueue);
            localUnSeqBOMap.put(unSeqBO.getId(), unSeqBO);
        }
    }

    /**
     * bean初始化的时候，回调该方法
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<IdGenerateConfigPO> poList = idGenerateMapper.selectList(null);
        for (IdGenerateConfigPO idGeneratePO : poList) {
            tryUpdateMySqlRecord(idGeneratePO);
            // 初始化信号量，每次只能有一个线程进行抢占
            semaphoreMap.put(idGeneratePO.getId(), new Semaphore(1));
        }
    }
}
