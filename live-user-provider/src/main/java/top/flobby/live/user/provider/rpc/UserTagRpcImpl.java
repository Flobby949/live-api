package top.flobby.live.user.provider.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import top.flobby.live.user.constants.UserTagsEnum;
import top.flobby.live.user.interfaces.IUserTagRpc;
import top.flobby.live.user.provider.service.IUserTagService;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 实现类
 * @create : 2023-11-28 12:01
 **/

@DubboService
public class UserTagRpcImpl implements IUserTagRpc {

    @Resource
    private IUserTagService userTagService;

    @Override
    public boolean setTag(Long userId, UserTagsEnum userTagsEnum) {
        return userTagService.setTag(userId, userTagsEnum);
    }

    @Override
    public boolean cancelTag(Long userId, UserTagsEnum userTagsEnum) {
        return userTagService.cancelTag(userId, userTagsEnum);
    }

    @Override
    public boolean containsTag(Long userId, UserTagsEnum userTagsEnum) {
        return userTagService.containsTag(userId, userTagsEnum);
    }
}
