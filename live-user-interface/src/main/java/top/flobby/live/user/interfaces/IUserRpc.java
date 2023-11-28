package top.flobby.live.user.interfaces;

import top.flobby.live.user.dto.UserDTO;

import java.util.List;
import java.util.Map;

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

    /**
     * 更新用户信息
     *
     * @param userDTO 用户 dto
     * @return boolean
     */
    boolean updateUserInfo(UserDTO userDTO);

    /**
     * 插入用户
     *
     * @param userDTO 用户 dto
     * @return boolean
     */
    boolean insertUser(UserDTO userDTO);

    Map<Long, UserDTO> batchQueryUserInfo(List<Long> userIdList);
}
