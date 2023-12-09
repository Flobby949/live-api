package top.flobby.live.im.core.server.service;

import top.flobby.live.im.dto.ImMsgBody;

/**
 * @author : Flobby
 * @program : live-api
 * @description : router 处理
 * @create : 2023-12-08 13:44
 **/

public interface IRouterHandlerService {

    /**
     * 收到业务服务的请求，进行处理
     *
     * @param msgBody msg
     */
    void onReceive(ImMsgBody msgBody);

    /**
     * 发送消息到客户端
     *
     * @param msgBody msg
     */
    boolean sendMsgToClient(ImMsgBody msgBody);
}
