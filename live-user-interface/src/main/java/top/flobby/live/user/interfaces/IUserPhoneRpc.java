package top.flobby.live.user.interfaces;

import top.flobby.live.user.dto.UserLoginDTO;
import top.flobby.live.user.dto.UserPhoneDTO;

import java.util.List;

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
