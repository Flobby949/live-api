package top.flobby.live.user.provider.rpc;

import org.apache.dubbo.config.annotation.DubboService;
import top.flobby.live.user.dto.UserLoginDTO;
import top.flobby.live.user.interfaces.IUserPhoneRpc;

/**
 * @author : Flobby
 * @program : live-api
 * @description :
 * @create : 2023-12-03 14:25
 **/

@DubboService
public class UserPhoneRpcImpl implements IUserPhoneRpc {

    @Override
    public UserLoginDTO login(String phone) {
        return null;
    }
}
