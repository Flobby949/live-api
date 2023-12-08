package top.flobby.live.im.router.provider;

import com.alibaba.fastjson2.JSON;
import jakarta.annotation.Resource;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import top.flobby.live.common.annotation.LiveApplication;
import top.flobby.live.im.dto.ImMsgBody;
import top.flobby.live.im.router.provider.service.ImRouterService;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 启动类
 * @create : 2023-12-08 10:18
 **/

@LiveApplication
// @EnableDubbo
public class ImRouterApplication implements CommandLineRunner {

    @Resource
    private ImRouterService imRouterService;

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(ImRouterApplication.class);
        springApplication.setWebApplicationType(WebApplicationType.NONE);
        springApplication.run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        for (int i = 0; i < 100; i++) {
            ImMsgBody imMsgBody = ImMsgBody.builder().build();
            imRouterService.sendMsg(10001L, JSON.toJSONString(imMsgBody));
            Thread.sleep(2000);
        }
    }
}
