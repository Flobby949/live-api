package top.flobby.live.user.provider.service;

import top.flobby.live.user.interfaces.dto.UserDTO;

/**
 * @author : Flobby
 * @program : live-api
 * @description : service
 * @create : 2023-11-18 14:44
 **/

public interface IUserService {

    /**
     * 按用户 ID 获取
     *
     * @param userId 用户 ID
     * @return {@link UserDTO}
     */
    UserDTO getByUserId(Long userId);
}
