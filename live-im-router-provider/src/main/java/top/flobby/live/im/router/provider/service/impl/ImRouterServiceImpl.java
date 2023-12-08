package top.flobby.live.im.router.provider.service.impl;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.rpc.RpcContext;
import org.springframework.stereotype.Service;
import top.flobby.live.im.core.server.interfaces.IRouterHandlerRpc;
import top.flobby.live.im.router.provider.service.ImRouterService;

/**
 * @author : Flobby
 * @program : live-api
 * @description :
 * @create : 2023-12-08 10:26
 **/

@Service
public class ImRouterServiceImpl implements ImRouterService {

    @DubboReference
    private IRouterHandlerRpc iRouterHandlerRpc;

    @Override
    public boolean sendMsg(Long objectId, String msgJson) {
        RpcContext.getContext().set("ip", "10.21.24.220:9010");
        iRouterHandlerRpc.sendMsg(objectId, msgJson);
        return true;
    }
}
