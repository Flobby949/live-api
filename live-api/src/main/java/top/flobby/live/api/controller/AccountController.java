package top.flobby.live.api.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import top.flobby.live.account.interfaces.IAccountRpc;
import top.flobby.live.common.constants.RequestHeaderConstant;
import top.flobby.live.common.exception.BusinessException;
import top.flobby.live.common.exception.BusinessExceptionEnum;
import top.flobby.live.common.resp.CommonResp;
import top.flobby.live.msg.dto.MsgCheckDTO;
import top.flobby.live.msg.interfaces.ISmsRpc;
import top.flobby.live.user.dto.UserLoginDTO;
import top.flobby.live.user.interfaces.IUserPhoneRpc;
import top.flobby.live.user.req.UserLoginReq;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 账户接口
 * @create : 2023-11-17 16:03
 **/

@Slf4j
@RestController
@RequestMapping("/account")
public class AccountController {

    @DubboReference
    private IUserPhoneRpc userPhoneRpc;
    @DubboReference
    private ISmsRpc smsRpc;
    @DubboReference
    private IAccountRpc accountRpc;

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
        accountRpc.logout(token);
        return CommonResp.success(null);
    }

}
