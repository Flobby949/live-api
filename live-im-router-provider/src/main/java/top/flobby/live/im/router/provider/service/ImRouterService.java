package top.flobby.live.im.router.provider.service;

import top.flobby.live.im.dto.ImMsgBody;

import java.util.List;

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
     * @param imMsgBody 消息
     * @return boolean
     */
    boolean sendMsg(ImMsgBody imMsgBody);


    /**
     * 批量发送消息，群聊场景
     *
     * @param imMsgBodyList list
     */
    void batchSendMsg(List<ImMsgBody> imMsgBodyList);
}
