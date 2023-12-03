package top.flobby.live.msg.provider.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import top.flobby.live.msg.dto.MsgCheckDTO;
import top.flobby.live.msg.enums.MsgSendResultEnum;
import top.flobby.live.msg.interfaces.ISmsRpc;
import top.flobby.live.msg.provider.service.ISmsService;

/**
 * @author : Flobby
 * @program : live-api
 * @description : rpc实现类
 * @create : 2023-12-03 13:03
 **/

@DubboService
public class SmsRpcImpl implements ISmsRpc {

    @Resource
    private ISmsService smsService;

    @Override
    public MsgSendResultEnum sendLoginMsg(String phone) {
        return smsService.sendLoginMsg(phone);
    }

    @Override
    public MsgCheckDTO checkLoginMsg(String phone, Integer code) {
        return smsService.checkLoginMsg(phone, code);
    }
}
