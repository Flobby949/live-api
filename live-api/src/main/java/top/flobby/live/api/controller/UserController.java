package top.flobby.live.api.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import top.flobby.live.common.constants.RequestHeaderConstant;
import top.flobby.live.common.exception.BusinessException;
import top.flobby.live.common.exception.BusinessExceptionEnum;
import top.flobby.live.common.resp.CommonResp;
import top.flobby.live.msg.dto.MsgCheckDTO;
import top.flobby.live.msg.interfaces.ISmsRpc;
import top.flobby.live.user.dto.UserDTO;
import top.flobby.live.user.dto.UserLoginDTO;
import top.flobby.live.user.interfaces.IUserPhoneRpc;
import top.flobby.live.user.interfaces.IUserRpc;
import top.flobby.live.user.req.UserLoginReq;
import top.flobby.live.web.starter.RequestContext;

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
    @DubboReference
    private IUserPhoneRpc userPhoneRpc;
    @DubboReference
    private ISmsRpc smsRpc;

    @PostMapping("/login")
    public CommonResp<UserLoginDTO> login(@RequestBody UserLoginReq req) {
        if (ObjectUtils.isEmpty(req)) {
            throw new BusinessException(BusinessExceptionEnum.PARAMS_ERROR);
        }
        String phone = req.getPhone();
        Integer code = req.getCode();
        // 校验验证码
        MsgCheckDTO msgCheckDTO = smsRpc.checkLoginMsg(phone, code);
        if (Boolean.FALSE.equals(msgCheckDTO.getCheckStatus())) {
            return CommonResp.error(msgCheckDTO.getDesc());
        }
        // 登录业务
        UserLoginDTO loginRes = userPhoneRpc.login(phone);
        if (Boolean.FALSE.equals(loginRes.getIsLoginSuccess())) {
            return CommonResp.error(loginRes.getMessage());
        }
        return CommonResp.success(loginRes);
    }

    @PostMapping("/logout")
    public CommonResp<Object> logout(@RequestHeader(RequestHeaderConstant.AUTHORIZATION) String token) {
        userPhoneRpc.logout(token);
        return CommonResp.success(null);
    }

    @GetMapping("/getUserInfo")
    public UserDTO getUserInfo() {
        Long userId = RequestContext.getUserId();
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
