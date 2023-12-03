package top.flobby.live.msg.provider.rpc;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import top.flobby.live.common.exception.BusinessException;
import top.flobby.live.common.exception.BusinessExceptionEnum;
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

@Slf4j
@DubboService
public class SmsRpcImpl implements ISmsRpc {

    @Resource
    private ISmsService smsService;

    @Override
    public void sendLoginMsg(String phone) {
        MsgSendResultEnum result = smsService.sendLoginMsg(phone);
        if (result != MsgSendResultEnum.SEND_SUCCESS) {
            log.info("短信发送失败, phone:{}, 错误原因： {}", phone, result.getDesc());
            throw new BusinessException(BusinessExceptionEnum.CODE_SEND_FAIL);
        }
    }

    @Override
    public MsgCheckDTO checkLoginMsg(String phone, Integer code) {
        return smsService.checkLoginMsg(phone, code);
    }
}
