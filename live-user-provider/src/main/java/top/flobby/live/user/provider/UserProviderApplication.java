package top.flobby.live.user.provider;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import top.flobby.live.common.annotation.LiveApplication;
import top.flobby.live.user.provider.service.IUserPhoneService;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 启动类
 * @create : 2023-11-17 15:36
 **/

@Slf4j
@LiveApplication
@EnableDubbo
public class UserProviderApplication implements CommandLineRunner {

    @Resource
    private IUserPhoneService userService;

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(UserProviderApplication.class);
        springApplication.setWebApplicationType(WebApplicationType.NONE);
        springApplication.run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        long userId = 10002L;
        // String phone = "18962521754";
        // UserLoginDTO user1 = userService.login(phone);
        // UserLoginDTO user2 = userService.login(phone);
        // System.out.println(user1);
        // System.out.println(user2);
        // System.out.println("========================================");
        // System.out.println(userService.queryByUserId(user1.getUserId()));
        // System.out.println(userService.queryByUserId(user1.getUserId()));
        // System.out.println("========================================");
        // System.out.println(userService.queryByPhone(phone));
        // System.out.println(userService.queryByPhone(phone));
        // UserDTO userDTO = userService.getByUserId(userId);
        // userDTO.setNickName("test-nick-name");
        // userService.updateUserInfo(userDTO);
        // System.out.println(userTagService.containsTag(userId, UserTagsEnum.IS_RICH));
        // System.out.println(userTagService.setTag(userId, UserTagsEnum.IS_RICH));
        // System.out.println(userTagService.containsTag(userId, UserTagsEnum.IS_RICH));
        // System.out.println(userTagService.cancelTag(userId, UserTagsEnum.IS_RICH));
        // System.out.println(userTagService.containsTag(userId, UserTagsEnum.IS_RICH));
        // CountDownLatch countDownLatch = new CountDownLatch(1);
        // for (int i = 0; i < 100; i++) {
        //     new Thread(() -> {
        //         try {
        //             countDownLatch.await();
        //             log.info("设置金主标签：" + userTagService.setTag(userId, UserTagsEnum.IS_RICH));
        //         } catch (InterruptedException e) {
        //             throw new RuntimeException(e);
        //         }
        //     }).start();
        // }
        // countDownLatch.countDown();
        // System.out.println(userTagService.setTag(userId, UserTagsEnum.IS_RICH));
        // System.out.println(userTagService.setTag(userId, UserTagsEnum.IS_RICH));
        // // System.out.println("是否包含金主标签：" + userTagService.containsTag(userId, UserTagsEnum.IS_RICH));
        // // System.out.println(userTagService.setTag(userId, UserTagsEnum.IS_VIP));
        // // System.out.println("是否包含VIP标签：" + userTagService.containsTag(userId, UserTagsEnum.IS_VIP));
        // // System.out.println(userTagService.setTag(userId, UserTagsEnum.IS_OLD_USER));
        // // System.out.println("是否包含老用户标签：" + userTagService.containsTag(userId, UserTagsEnum.IS_OLD_USER));
        // System.out.println(userTagService.cancelTag(userId, UserTagsEnum.IS_RICH));
        // System.out.println(userTagService.cancelTag(userId, UserTagsEnum.IS_RICH));
    }
}
