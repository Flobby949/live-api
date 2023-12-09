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
     * @param msgBody 味精体
     */
    void onReceive(ImMsgBody msgBody);
}
