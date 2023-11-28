package top.flobby.live.user.provider.rpc;

import org.apache.dubbo.config.annotation.DubboService;
import top.flobby.live.user.constants.UserTagsEnum;
import top.flobby.live.user.interfaces.IUserTagRpc;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 实现类
 * @create : 2023-11-28 12:01
 **/

@DubboService
public class UserTagRpcImpl implements IUserTagRpc {
    @Override
    public boolean setTag(Long userId, UserTagsEnum userTagsEnum) {
        return false;
    }

    @Override
    public boolean cancelTag(Long userId, UserTagsEnum userTagsEnum) {
        return false;
    }

    @Override
    public boolean containsTag(Long userId, UserTagsEnum userTagsEnum) {
        return false;
    }
}
