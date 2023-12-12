package top.flobby.live.living.provider;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import top.flobby.live.common.annotation.LiveApplication;
import top.flobby.live.living.dto.LivingRoomReqDTO;
import top.flobby.live.living.provider.service.ILivingRoomService;

/**
 * @author : Flobby
 * @program : live-api
 * @description :
 * @create : 2023-12-12 09:37
 **/

@LiveApplication
@EnableDubbo
public class LivingProviderApplication implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(LivingProviderApplication.class);
        springApplication.setWebApplicationType(WebApplicationType.NONE);
        springApplication.run(args);
    }

    @Resource
    private ILivingRoomService livingRoomService;

    @Override
    public void run(String... args) throws Exception {
        LivingRoomReqDTO livingRoomReqDTO = new LivingRoomReqDTO();
        livingRoomReqDTO.setAnchorId(5L);
        livingRoomReqDTO.setRoomName("测试直播间");
        livingRoomReqDTO.setCovertImg("https://www.baidu.com");
        livingRoomReqDTO.setType(1);
        Integer roomId = livingRoomService.startLivingRoom(livingRoomReqDTO);
        System.out.println(roomId);
        System.out.println(livingRoomService.queryByRoomId(roomId));
        livingRoomReqDTO.setId(roomId);
        Thread.sleep(3000);
        System.out.println(livingRoomService.closeLiving(livingRoomReqDTO));
    }
}
