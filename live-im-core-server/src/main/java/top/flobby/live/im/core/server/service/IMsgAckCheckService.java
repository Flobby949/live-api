package top.flobby.live.im.core.server.service;

import top.flobby.live.im.dto.ImMsgBody;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 消息确认service
 * @create : 2023-12-09 14:05
 **/

public interface IMsgAckCheckService {

    /**
     * 客户端发送ACK包到服务端后，调用方法对ACK记录进行处理
     *
     * @param msgBody msg
     */
    void doMsgCheck(ImMsgBody msgBody);

    /**
     * 记录 ACK 消息
     *
     * @param msgBody msg
     * @param times   times
     */
    void recordAckMsg(ImMsgBody msgBody, Integer times);

    /**
     * 发送延迟消息,用于进行消息重试
     *
     * @param msgBody msg
     */
    void sendDelayMsg(ImMsgBody msgBody);

    /**
     * 获取 ACK 消息的重试次数
     *
     * @param msgBody msg
     * @return int
     */
    int getAckMsgTimes(ImMsgBody msgBody);
}
