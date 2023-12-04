package top.flobby.live.user.provider.service;

import top.flobby.live.user.dto.UserLoginDTO;
import top.flobby.live.user.dto.UserPhoneDTO;

import java.util.List;

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
     * 注销
     *
     * @param token 令 牌
     */
    void logout(String token);

    /**
     * 通过手机号查询用户
     *
     * @param phone 电话
     * @return {@link UserPhoneDTO}
     */
    UserPhoneDTO queryByPhone(String phone);

    /**
     * 按用户ID查询
     *
     * @param userId 用户 ID
     * @return {@link List}<{@link UserPhoneDTO}>
     */
    List<UserPhoneDTO> queryByUserId(Long userId);
}
