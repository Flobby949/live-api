package top.flobby.live.id.generate.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import top.flobby.live.id.generate.service.IdGenerateService;
import top.flobby.live.id.interfaces.IdGenerateRpc;

/**
 * @author : Flobby
 * @program : live-api
 * @description : rpc
 * @create : 2023-11-20 11:10
 **/

@DubboService
public class IdGenerateRpcImpl implements IdGenerateRpc {

    @Resource
    private IdGenerateService idGenerateService;

    @Override
    public Long getSeqId(Integer id) {
        return idGenerateService.getSeqId(id);
    }

    @Override
    public Long getUnSeqId(Integer id) {
        return idGenerateService.getUnSeqId(id);
    }
}
