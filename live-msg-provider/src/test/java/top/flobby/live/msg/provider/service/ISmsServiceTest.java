package top.flobby.live.msg.provider.service;

import jakarta.annotation.Resource;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ISmsServiceTest {

    @Resource
    private ISmsService smsService;

    @org.junit.jupiter.api.Test
    void sendLoginMsg() {
        String phone = "18962521753";
        System.out.println(smsService.sendLoginMsg(phone));
    }

    @org.junit.jupiter.api.Test
    void checkLoginMsg() {
        String phone = "18962521753";
        int code = 578213;
        System.out.println(smsService.checkLoginMsg(phone, code));
    }
}