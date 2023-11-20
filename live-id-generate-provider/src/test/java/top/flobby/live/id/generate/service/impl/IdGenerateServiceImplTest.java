package top.flobby.live.id.generate.service.impl;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import top.flobby.live.id.generate.service.IdGenerateService;

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
    }
}