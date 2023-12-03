package top.flobby.live.user.provider.service;

import top.flobby.live.user.dto.UserLoginDTO;
import top.flobby.live.user.dto.UserPhoneDTO;

/**
 * @author : Flobby
 * @program : live-api
 * @description : service
 * @create : 2023-12-03 14:28
 **/

public interface IUserPhoneService {

    /**
     * 登录
     *
     * @param phone 电话
     * @return {@link UserLoginDTO}
     */
    UserLoginDTO login(String phone);

    /**
     * 通过手机号查询用户
     *
     * @param phone 电话
     * @return {@link UserPhoneDTO}
     */
    UserPhoneDTO queryByPhone(String phone);
}
