package top.flobby.live.api.service.impl;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import top.flobby.live.api.service.ImService;
import top.flobby.live.api.vo.ImConfigVO;
import top.flobby.live.im.common.AppIdEnum;
import top.flobby.live.im.interfaces.ImTokenRpc;
import top.flobby.live.web.starter.context.RequestContext;

import java.util.Collections;
import java.util.List;

/**
 * @author : Flobby
 * @program : live-api
 * @description : impl
 * @create : 2023-12-13 17:59
 **/

@Slf4j
@Service
public class ImServiceImpl implements ImService {

    @DubboReference
    private ImTokenRpc imTokenRpc;
    @Resource
    private DiscoveryClient discoveryClient;

    @Override
    public ImConfigVO getImConfig() {
        ImConfigVO result = new ImConfigVO();
        result.setToken(imTokenRpc.createImLoginToken(RequestContext.getUserId(), AppIdEnum.LIVE_BIZ_ID.getCode()));
        result = getImServerAddress(result);
        return result;
    }

    private ImConfigVO getImServerAddress(ImConfigVO vo) {
        List<ServiceInstance> instances = discoveryClient.getInstances("live-im-core-server");
        if (CollectionUtils.isEmpty(instances)) {
            log.error("获取IM服务地址失败");
            return null;
        }
        Collections.shuffle(instances);
        ServiceInstance aimInstance = instances.get(0);
        String host = aimInstance.getHost();
        String wsAddress = host + ":8877";
        vo.setWsImServerAddress(wsAddress);
        String tcpAddress = host + ":8888";
        vo.setTcpImServerAddress(tcpAddress);
        return vo;
    }
}
