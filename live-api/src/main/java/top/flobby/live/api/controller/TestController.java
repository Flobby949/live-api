package top.flobby.live.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.flobby.live.common.exception.BusinessException;
import top.flobby.live.common.exception.BusinessExceptionEnum;
import top.flobby.live.common.resp.CommonResp;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 测试接口
 * @create : 2023-12-02 13:42
 **/

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("success")
    public CommonResp<String> test() {
        return CommonResp.success();
    }

    @GetMapping("error")
    public CommonResp<String> error() {
        return CommonResp.error();
    }

    @GetMapping("exception")
    public CommonResp<String> exception() {
        throw new BusinessException(BusinessExceptionEnum.OTHER_ERROR);
    }
}
