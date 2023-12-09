package top.flobby.live.im.router.interfaces;

import top.flobby.live.im.dto.ImMsgBody;

/**
 * @author : Flobby
 * @program : live-api
 * @description : rpc
 * @create : 2023-12-08 10:19
 **/

public interface ImRouterRpc {

    /**
     * 发送消息
     *
     * @param imMsgBody 消息 json
     * @return boolean
     */
    boolean sendMsg(ImMsgBody imMsgBody);
}
