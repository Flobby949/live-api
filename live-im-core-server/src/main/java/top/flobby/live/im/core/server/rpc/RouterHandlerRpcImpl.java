package top.flobby.live.im.core.server.rpc;

import org.apache.dubbo.config.annotation.DubboService;
import top.flobby.live.im.core.server.interfaces.IRouterHandlerRpc;

/**
 * @author : Flobby
 * @program : live-api
 * @description : impl
 * @create : 2023-12-08 10:15
 **/

@DubboService
public class RouterHandlerRpcImpl implements IRouterHandlerRpc {

    @Override
    public void sendMsg(Long userId, String msgJson) {
        System.out.println("this is im-core-server");
    }
}
