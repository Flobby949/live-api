package top.flobby.live.msg.interfaces;

import top.flobby.live.msg.dto.MsgCheckDTO;
import top.flobby.live.msg.enums.MsgSendResultEnum;

/**
 * @author : Flobby
 * @program : live-api
 * @description : rpc
 * @create : 2023-12-03 13:04
 **/

public interface ISmsRpc {

    /**
     * 发送登录验证码
     *
     * @param phone 电话
     * @return {@link MsgSendResultEnum}
     */
    MsgSendResultEnum sendLoginMsg(String phone);

    /**
     * 校验登录验证码
     *
     * @param phone 电话
     * @param code  验证码
     * @return {@link MsgCheckDTO}
     */
    MsgCheckDTO checkLoginMsg(String phone, Integer code);
}
