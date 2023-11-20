package top.flobby.live.id.generate.service.impl;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import top.flobby.live.id.generate.dao.mapper.IdGenerateMapper;
import top.flobby.live.id.generate.dao.po.IdGenerateConfigPO;
import top.flobby.live.id.generate.service.IdGenerateService;
import top.flobby.live.id.generate.service.bo.LocalSeqBO;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
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

    private static Map<Integer, LocalSeqBO> localSeqBOMap = new ConcurrentHashMap<>();

    @Resource
    private IdGenerateMapper idGenerateMapper;

    @Override
    public Long getSeqId(Integer id) {
        if (id == null) {
            log.error("[getSeqId] id is error, id is null");
            return null;
        }
        LocalSeqBO localSeqBO = localSeqBOMap.get(id);
        if (localSeqBO == null) {
            log.error("[getSeqId] id is error, localSeqBO is null");
            return null;
        }
        return localSeqBO.getCurrentNum().getAndIncrement();
    }

    @Override
    public Long getUnSeqId(Integer id) {
        return null;
    }


    /**
     * bean初始化的时候，回调该方法
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<IdGenerateConfigPO> poList = idGenerateMapper.selectList(null);
        for (IdGenerateConfigPO idGeneratePO : poList) {
            tryUpdateMySqlRecord(idGeneratePO);
        }
    }

    /**
     * 进行 id 段抢占
     *
     * @param idGeneratePO po
     */
    private void tryUpdateMySqlRecord(IdGenerateConfigPO idGeneratePO) {
        // 提取当前值
        long currentStart = idGeneratePO.getCurrentStart();
        long nextThreshold = idGeneratePO.getNextThreshold();
        int updateResult = idGenerateMapper.updateNewIdAndVersion(idGeneratePO.getId(), idGeneratePO.getVersion());
        // 更新成功，说明当前线程抢占成功
        if (updateResult > 0) {
            LocalSeqBO localSeqBO = new LocalSeqBO();
            localSeqBO.setId(idGeneratePO.getId());
            localSeqBO.setCurrentNum(new AtomicLong(currentStart));
            localSeqBO.setCurrentStart(currentStart);
            localSeqBO.setNextThreshold(nextThreshold);
            localSeqBOMap.put(localSeqBO.getId(), localSeqBO);
            return;
        }
        // 更新失败，说明有其他线程已经更新过了，进行重试
        for (int i = 0; i < 3; i++) {
            idGeneratePO = idGenerateMapper.selectById(idGeneratePO.getId());
            updateResult = idGenerateMapper.updateNewIdAndVersion(idGeneratePO.getId(), idGeneratePO.getVersion());
            if (updateResult == 0) {
                LocalSeqBO localSeqBO = new LocalSeqBO();
                localSeqBO.setId(idGeneratePO.getId());
                localSeqBO.setCurrentNum(new AtomicLong(idGeneratePO.getCurrentStart()));
                localSeqBO.setCurrentStart(idGeneratePO.getCurrentStart());
                localSeqBO.setNextThreshold(idGeneratePO.getNextThreshold());
                localSeqBOMap.put(localSeqBO.getId(), localSeqBO);
                return;
            }
        }
        throw new RuntimeException("[tryUpdateMySqlRecord] update fail,表id段抢占失败");
    }
}
