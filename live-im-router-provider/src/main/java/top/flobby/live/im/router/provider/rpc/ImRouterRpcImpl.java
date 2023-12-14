package top.flobby.live.im.router.provider.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import top.flobby.live.im.dto.ImMsgBody;
import top.flobby.live.im.router.interfaces.ImRouterRpc;
import top.flobby.live.im.router.provider.service.ImRouterService;

import java.util.List;

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
    public boolean sendMsg(ImMsgBody imMsgBody) {
        return imRouterService.sendMsg(imMsgBody);
    }

    @Override
    public void batchSendMsg(List<ImMsgBody> imMsgBodyList) {
        // 如果用for循环调用sendMsg方法，延迟很高，不建议用for循环的远程调用
        imRouterService.batchSendMsg(imMsgBodyList);
    }
}
