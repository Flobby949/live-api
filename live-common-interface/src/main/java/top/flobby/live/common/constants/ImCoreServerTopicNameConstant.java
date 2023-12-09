package top.flobby.live.common.constants;

/**
 * @author : Flobby
 * @program : live-api
 * @description : IM服务topic常量维护
 * @create : 2023-12-08 09:16
 **/

public interface ImCoreServerTopicNameConstant {
    /**
     * 接收 IM 系统发送的业务消息
     */
    String LIVE_IM_BIZ_MSG_TOPIC = "live_im_biz_msg_topic";

    /**
     * 接收 IM ACK 消息
     */
    String LIVE_IM_ACK_MSG_TOPIC = "live_im_ack_msg_topic";
}
