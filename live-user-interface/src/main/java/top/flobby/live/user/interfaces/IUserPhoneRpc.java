package top.flobby.live.user.interfaces;

import top.flobby.live.user.dto.UserLoginDTO;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 用户手机相关
 * @create : 2023-12-03 14:20
 **/


public interface IUserPhoneRpc {

    /**
     * 登录
     *
     * @param phone 电话
     * @return {@link UserLoginDTO}
     */
    UserLoginDTO login(String phone);
}
