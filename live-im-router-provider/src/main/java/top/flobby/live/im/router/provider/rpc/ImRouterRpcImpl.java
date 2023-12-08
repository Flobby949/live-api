package top.flobby.live.im.router.provider.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import top.flobby.live.im.router.interfaces.ImRouterRpc;
import top.flobby.live.im.router.provider.service.ImRouterService;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 实现类
 * @create : 2023-12-08 10:20
 **/

@DubboService
public class ImRouterRpcImpl implements ImRouterRpc {

    @Resource
    private ImRouterService imRouterService;

    @Override
    public boolean sendMsg(Long objectId, String msgJson) {
        return imRouterService.sendMsg(objectId, msgJson);
    }
}
