package top.flobby.live.im.router.provider.service.impl;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.rpc.RpcContext;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import top.flobby.live.common.constants.ImCoreServerConstant;
import top.flobby.live.im.core.server.interfaces.IRouterHandlerRpc;
import top.flobby.live.im.dto.ImMsgBody;
import top.flobby.live.im.router.provider.service.ImRouterService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author : Flobby
 * @program : live-api
 * @description :
 * @create : 2023-12-08 10:26
 **/

@Slf4j
@Service
public class ImRouterServiceImpl implements ImRouterService {

    @DubboReference
    private IRouterHandlerRpc routerHandlerRpc;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean sendMsg(ImMsgBody imMsgBody) {
        // 假设有 100 个userId，绑定在 10 台服务器上，最终绑定的IP一定 <= 10
        Long objectId = imMsgBody.getUserId();
        Integer appId = imMsgBody.getAppId();
        String bindAddress = stringRedisTemplate.opsForValue().get(ImCoreServerConstant.IM_BIND_IP_KEY + appId + ":" + objectId);
        if (StringUtils.isEmpty(bindAddress)) {
            return false;
        }
        // 保存进去的ip是 ip%userId，截取ip地址
        bindAddress = bindAddress.substring(0, bindAddress.indexOf("%"));
        log.info("[ImRouterServiceImpl] sendMsg,ip:{},msg:{}", bindAddress, imMsgBody);
        RpcContext.getContext().set("ip", bindAddress);
        routerHandlerRpc.sendMsg(objectId, imMsgBody);
        return true;
    }

    @Override
    public void batchSendMsg(List<ImMsgBody> imMsgBodyList) {
        // 保证appId是统一的，根据userId，获取缓存的key·
        List<String> cacheKeyList = imMsgBodyList.stream()
                .map(immsgBody -> ImCoreServerConstant.IM_BIND_IP_KEY + immsgBody.getAppId() + ":" + immsgBody.getUserId())
                .collect(Collectors.toList());
        // 从缓存冲批量获取ip地址
        List<String> ipList = stringRedisTemplate.opsForValue().multiGet(cacheKeyList);
        if (CollectionUtils.isEmpty(ipList)) {
            return;
        }
        Map<String, List<Long>> userIdMap = new HashMap<>();
        // 按ip分组
        ipList.forEach(address -> {
            String[] addressInfoArray = address.split("%");
            String currentAddress = addressInfoArray[0];
            List<Long> currentUserIdMapValue = userIdMap.getOrDefault(currentAddress, new ArrayList<>());
            currentUserIdMapValue.add(Long.parseLong(addressInfoArray[1]));
            userIdMap.put(currentAddress, currentUserIdMapValue);
        });
        // 把msgBody按userId分组
        Map<Long, ImMsgBody> userIdMsgMap = imMsgBodyList.stream().collect(Collectors.toMap(ImMsgBody::getUserId, x -> x));
        // 将连接同一台IM服务器的消息，进行统一发送
        userIdMap.keySet().forEach(currentIp -> {
            RpcContext.getContext().set("ip", currentIp);
            // ip 绑定的userId集合
            List<Long> ipBindUserIdList = userIdMap.get(currentIp);
            // 根据ip分组构造消息体集合
            List<ImMsgBody> batchSendMsgGroupByIpList = ipBindUserIdList.stream()
                    .map(userIdMsgMap::get)
                    .collect(Collectors.toList());
            routerHandlerRpc.batchSendMsg(batchSendMsgGroupByIpList);
        });
    }
}
