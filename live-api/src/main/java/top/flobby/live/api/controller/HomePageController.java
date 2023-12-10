package top.flobby.live.api.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.flobby.live.api.service.HomePageService;
import top.flobby.live.api.vo.HomePageUserVO;
import top.flobby.live.common.resp.CommonResp;
import top.flobby.live.web.starter.RequestContext;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 主页接口
 * @create : 2023-12-10 14:24
 **/

@RestController
@RequestMapping("/home-page")
public class HomePageController {

    @Resource
    private HomePageService homePageService;

    @GetMapping("/user-info")
    public CommonResp<HomePageUserVO> getHomePageUserInfo() {
        Long userId = RequestContext.getUserId();
        HomePageUserVO result = homePageService.getHomePageUserInfo(userId);
        return CommonResp.success(result);
    }

}
