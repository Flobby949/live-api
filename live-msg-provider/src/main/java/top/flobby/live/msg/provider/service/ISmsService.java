package top.flobby.live.msg.provider.service;

import top.flobby.live.msg.dto.MsgCheckDTO;
import top.flobby.live.msg.enums.MsgSendResultEnum;
import top.flobby.live.msg.enums.SmsSendTypeEnum;

/**
 * @author : Flobby
 * @program : live-api
 * @description : service
 * @create : 2023-12-03 13:02
 **/

public interface ISmsService {

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

    /**
     * 插入一条短信记录
     *
     * @param phone 电话
     * @param code  验证码
     * @param type  类型枚举
     */
    void insertMsg(String phone, Integer code, SmsSendTypeEnum type);
}
