package top.flobby.live.api.service;

import top.flobby.live.api.vo.HomePageUserVO;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 主页服务
 * @create : 2023-12-10 14:25
 **/


public interface HomePageService {

    /**
     * 获取主页用户信息
     *
     * @param userId userId
     * @return {@link HomePageUserVO}
     */
    HomePageUserVO getHomePageUserInfo(Long userId);
}
