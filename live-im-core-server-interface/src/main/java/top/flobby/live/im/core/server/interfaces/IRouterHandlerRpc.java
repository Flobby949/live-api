package top.flobby.live.im.core.server.interfaces;

import top.flobby.live.im.dto.ImMsgBody;

import java.util.List;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 给 router 层调用的rpc
 * @create : 2023-12-08 10:14
 **/

public interface IRouterHandlerRpc {

    /**
     * 发送消息
     *
     * @param userId  用户 ID
     * @param msgBody 消息 json
     */
    void sendMsg(Long userId, ImMsgBody msgBody);

    void batchSendMsg(List<ImMsgBody> imMsgBodyList);
}
