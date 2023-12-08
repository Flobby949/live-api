package top.flobby.live.im.router.provider.service;

/**
 * @author : Flobby
 * @program : live-api
 * @description : service
 * @create : 2023-12-08 10:26
 **/

public interface ImRouterService {

    /**
     * 发送消息
     *
     * @param objectId 目标 ID
     * @param msgJson  消息 json
     * @return boolean
     */
    boolean sendMsg(Long objectId, String msgJson);
}
