package top.flobby.live.api.controller;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.flobby.live.user.interfaces.IUserRpc;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 测试接口
 * @create : 2023-11-17 16:03
 **/

@RestController
@RequestMapping("/test")
public class TestController {

    @DubboReference(group = "test")
    private IUserRpc userRpc;

    @GetMapping("/dubbo")
    public String testDubbo(){
        return userRpc.test();
    }
}
