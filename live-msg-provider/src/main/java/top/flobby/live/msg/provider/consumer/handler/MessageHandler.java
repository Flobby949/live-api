package top.flobby.live.msg.provider.consumer.handler;

import top.flobby.live.im.dto.ImMsgBody;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 消息处理
 * @create : 2023-12-08 13:52
 **/

public interface MessageHandler {

    /**
     * MSG 接收
     *
     * @param msgBody msg
     */
    void onMsgReceive(ImMsgBody msgBody);
}
