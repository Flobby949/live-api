package top.flobby.live.im.provider.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import top.flobby.live.im.interfaces.ImTokenRpc;
import top.flobby.live.im.provider.service.ImTokenService;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 实现类
 * @create : 2023-12-07 16:54
 **/

@DubboService
public class ImTokenRpcImpl implements ImTokenRpc {

    @Resource
    private ImTokenService imTokenService;

    @Override
    public String createImLoginToken(Long userId, Integer appId) {
        return imTokenService.createImLoginToken(userId, appId);
    }

    @Override
    public Long getUserIdByToken(String token) {
        return imTokenService.getUserIdByToken(token);
    }

    @Override
    public Integer getAppIdByToken(String token) {
        return imTokenService.getAppIdByToken(token);
    }
}
