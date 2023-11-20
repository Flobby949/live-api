package top.flobby.live.id.generate.rpc;

import org.apache.dubbo.config.annotation.DubboService;
import top.flobby.live.id.interfaces.IdGenerateRpc;

/**
 * @author : Flobby
 * @program : live-api
 * @description : rpc
 * @create : 2023-11-20 11:10
 **/

@DubboService
public class IdGenerateRpcImpl implements IdGenerateRpc {


    @Override
    public Long getSeqId(Integer id) {
        return null;
    }

    @Override
    public Long getUnSeqId(Integer id) {
        return null;
    }
}
