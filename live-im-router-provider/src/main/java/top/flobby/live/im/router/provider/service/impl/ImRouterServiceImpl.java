package top.flobby.live.im.router.provider.service.impl;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.rpc.RpcContext;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import top.flobby.live.im.core.server.constant.ImCoreServerConstant;
import top.flobby.live.im.core.server.interfaces.IRouterHandlerRpc;
import top.flobby.live.im.dto.ImMsgBody;
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
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean sendMsg(ImMsgBody imMsgBody) {
        Long objectId = imMsgBody.getUserId();
        Integer appId = imMsgBody.getAppId();
        String bindAddress = stringRedisTemplate.opsForValue().get(ImCoreServerConstant.IM_BIND_IP_KEY + appId + ":" + objectId);
        if (StringUtils.isEmpty(bindAddress)) {
            return false;
        }
        RpcContext.getContext().set("ip", bindAddress);
        iRouterHandlerRpc.sendMsg(objectId, imMsgBody);
        return true;
    }
}
