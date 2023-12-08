package top.flobby.live.im.router.interfaces;

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
     * @param objectId 目标 ID
     * @param msgJson  消息 json
     * @return boolean
     */
    boolean sendMsg(Long objectId, String msgJson);
}
