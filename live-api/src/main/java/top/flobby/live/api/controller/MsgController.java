package top.flobby.live.api.controller;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.flobby.live.common.resp.CommonResp;
import top.flobby.live.msg.interfaces.ISmsRpc;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 消息服务
 * @create : 2023-12-03 16:57
 **/

@RestController
@RequestMapping("/msg")
public class MsgController {

    @DubboReference
    private ISmsRpc smsRpc;

    @PostMapping("/send")
    public CommonResp<String> send(@RequestParam String phone) {
        smsRpc.sendLoginMsg(phone);
        return CommonResp.success();
    }
}
