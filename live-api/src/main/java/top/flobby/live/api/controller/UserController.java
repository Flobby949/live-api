package top.flobby.live.api.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.flobby.live.user.interfaces.IUserRpc;
import top.flobby.live.user.interfaces.dto.UserDTO;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 测试接口
 * @create : 2023-11-17 16:03
 **/

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @DubboReference
    private IUserRpc userRpc;

    @GetMapping("/getUserInfo")
    public UserDTO getUserInfo(Long userId) {
        log.info("userId:{}", userId);
        return userRpc.getByUserId(userId);
    }
}
