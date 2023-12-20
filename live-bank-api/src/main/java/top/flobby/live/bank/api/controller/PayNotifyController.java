package top.flobby.live.bank.api.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.flobby.live.bank.api.service.IPayNotifyService;
import top.flobby.live.common.resp.CommonResp;

/**
 * @author : Flobby
 * @program : live-api
 * @description :
 * @create : 2023-12-20 11:06
 **/

@RestController
@RequestMapping("/pay-notify")
public class PayNotifyController {

    @Resource
    private IPayNotifyService payNotifyService;

    @PostMapping("/wechat")
    public CommonResp<Object> weChatPayNotify(@RequestParam("param") String param) {
        return CommonResp.success(payNotifyService.notifyHandler(param));
    }
}
