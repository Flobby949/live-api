package top.flobby.live.user.interfaces;

import top.flobby.live.user.interfaces.dto.UserDTO;

/**
 * @author : Flobby
 * @program : live-api
 * @description : RPC接口
 * @create : 2023-11-17 15:49
 **/

public interface IUserRpc {
    /**
     * 按用户 ID 获取
     *
     * @param userId 用户 ID
     * @return {@link UserDTO}
     */
    UserDTO getByUserId(Long userId);
}
