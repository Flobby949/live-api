package top.flobby.live.im.core.server.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import top.flobby.live.im.core.server.interfaces.IRouterHandlerRpc;
import top.flobby.live.im.core.server.service.IRouterHandlerService;
import top.flobby.live.im.dto.ImMsgBody;

import java.util.List;

/**
 * @author : Flobby
 * @program : live-api
 * @description : impl
 * @create : 2023-12-08 10:15
 **/

@DubboService
public class RouterHandlerRpcImpl implements IRouterHandlerRpc {

    @Resource
    private IRouterHandlerService routerHandlerService;

    @Override
    public void sendMsg(Long userId, ImMsgBody msgBody) {
        routerHandlerService.onReceive(msgBody);
    }

    @Override
    public void batchSendMsg(List<ImMsgBody> imMsgBodyList) {
        // 目前在本地内存，性能更高
        imMsgBodyList.forEach(imMsgBody -> routerHandlerService.onReceive(imMsgBody));
    }
}
