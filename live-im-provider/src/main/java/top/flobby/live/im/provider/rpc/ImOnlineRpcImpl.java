package top.flobby.live.im.provider.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import top.flobby.live.im.interfaces.ImOnlineRpc;
import top.flobby.live.im.provider.service.ImOnlineService;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 用户在线监测RPC
 * @create : 2023-12-09 13:40
 **/

@DubboService
public class ImOnlineRpcImpl implements ImOnlineRpc {

    @Resource
    private ImOnlineService imOnlineService;

    @Override
    public boolean isOnline(Long userId, Integer appId) {
        return imOnlineService.isOnline(userId, appId);
    }
}
