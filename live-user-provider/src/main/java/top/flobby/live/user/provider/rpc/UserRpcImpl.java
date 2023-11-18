package top.flobby.live.user.provider.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import top.flobby.live.user.interfaces.IUserRpc;
import top.flobby.live.user.interfaces.dto.UserDTO;
import top.flobby.live.user.provider.service.IUserService;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 实现类
 * @create : 2023-11-17 15:50
 **/

@DubboService
public class UserRpcImpl implements IUserRpc {

    @Resource
    private IUserService userService;

    @Override
    public UserDTO getByUserId(Long userId) {
        return userService.getByUserId(userId);
    }
}
