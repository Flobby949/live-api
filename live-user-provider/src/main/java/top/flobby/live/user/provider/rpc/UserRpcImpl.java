package top.flobby.live.user.provider.rpc;

import org.apache.dubbo.config.annotation.DubboService;
import top.flobby.live.user.interfaces.IUserRpc;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 实现类
 * @create : 2023-11-17 15:50
 **/

@DubboService(group = "test")
public class UserRpcImpl implements IUserRpc {
    @Override
    public String test() {
        System.out.println("this is dubbo test");
        return "success";
    }
}
