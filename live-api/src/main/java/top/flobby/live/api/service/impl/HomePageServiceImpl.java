package top.flobby.live.api.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import top.flobby.live.api.service.HomePageService;
import top.flobby.live.api.vo.HomePageUserVO;
import top.flobby.live.common.utils.ConvertBeanUtils;
import top.flobby.live.user.constants.UserTagsEnum;
import top.flobby.live.user.dto.UserDTO;
import top.flobby.live.user.interfaces.IUserRpc;
import top.flobby.live.user.interfaces.IUserTagRpc;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 实现类
 * @create : 2023-12-10 14:25
 **/

@Slf4j
@Service
public class HomePageServiceImpl implements HomePageService {

    @DubboReference
    private IUserRpc userRpc;
    @DubboReference
    private IUserTagRpc userTagRpc;

    @Override
    public HomePageUserVO getHomePageUserInfo(Long userId) {
        UserDTO userInfo = userRpc.getByUserId(userId);
        HomePageUserVO result = ConvertBeanUtils.convert(userInfo, HomePageUserVO.class);
        boolean isVip = userTagRpc.containsTag(userId, UserTagsEnum.IS_VIP);
        result.setIsVip(isVip);
        return result;
    }
}
