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
        Long seqId = idGenerateService.getSeqId(1);
        System.out.println(seqId);
    }

    @Test
    void getUnSeqId() {
    }
}