package top.flobby.live.api.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.flobby.live.api.service.ImService;
import top.flobby.live.api.vo.ImConfigVO;
import top.flobby.live.common.resp.CommonResp;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 通信接口
 * @create : 2023-12-13 14:35
 **/

@RestController
@RequestMapping("/im")
public class ImController {

    @Resource
    private ImService imService;

    @GetMapping("/config")
    public CommonResp<ImConfigVO> getImConfig() {
        return CommonResp.success(imService.getImConfig());
    }
}
