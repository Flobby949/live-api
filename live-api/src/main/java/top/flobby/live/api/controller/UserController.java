package top.flobby.live.api.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.flobby.live.user.interfaces.IUserRpc;
import top.flobby.live.user.interfaces.dto.UserDTO;

import java.util.Arrays;
import java.util.Map;

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

    @GetMapping("batchQueryUserInfo")
    public Map<Long, UserDTO> batchQueryUserInfo(String userIdStr) {
        return userRpc.batchQueryUserInfo(Arrays.stream(userIdStr.split(",")).map(Long::valueOf).toList());
    }

    @GetMapping("/updateUserInfo")
    public boolean updateUserInfo(Long userId, String nickName) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(userId);
        userDTO.setNickName(nickName);
        return userRpc.updateUserInfo(userDTO);
    }

    @GetMapping("/insertUser")
    public boolean insertUser(Long userId) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(userId);
        userDTO.setNickName("live-test");
        userDTO.setSex(1);
        return userRpc.insertUser(userDTO);
    }
}
