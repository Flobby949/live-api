package top.flobby.live.id.generate.service.impl;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import top.flobby.live.id.generate.service.IdGenerateService;

import java.util.HashSet;

@SpringBootTest
class IdGenerateServiceImplTest {
    @Resource
    private IdGenerateService idGenerateService;

    @Test
    void getSeqId() {
        for (int i = 0; i < 200; i++) {
            Long seqId = idGenerateService.getSeqId(1);
            System.out.println(seqId);
        }
        // 多运行一会，让线程池中的线程都执行完
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getUnSeqId() {
        HashSet<Long> hashSet = new HashSet<>();
        for (int i = 0; i < 500; i++) {
            Long unSeqId = idGenerateService.getUnSeqId(1);
            hashSet.add(unSeqId);
        }
        // 多运行一会，让线程池中的线程都执行完
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(hashSet.size());
    }
}