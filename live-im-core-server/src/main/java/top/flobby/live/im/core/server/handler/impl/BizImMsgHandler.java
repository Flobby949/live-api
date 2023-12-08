package top.flobby.live.im.core.server.handler.impl;

import com.alibaba.fastjson2.JSON;
import io.netty.channel.ChannelHandlerContext;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import top.flobby.live.common.constants.ImCoreServerTopicNameConstant;
import top.flobby.live.im.core.server.common.ImMsg;
import top.flobby.live.im.core.server.handler.SimplyHandler;
import top.flobby.live.im.core.server.utils.ImContextUtils;
import top.flobby.live.im.dto.ImMsgBody;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 常规业务消息处理器
 * @create : 2023-12-07 15:33
 **/

@Slf4j
@Component
public class BizImMsgHandler implements SimplyHandler {

    @Resource
    private MQProducer mqProducer;

    @Override
    public void handler(ChannelHandlerContext ctx, ImMsg msg) {
        Long userId = ImContextUtils.getUserId(ctx);
        Integer appId = ImContextUtils.getAppId(ctx);
        if (ObjectUtils.isEmpty(userId) || ObjectUtils.isEmpty(appId)) {
            log.error("【BizImMsgHandler】, 参数异常, msg:{}", msg);
            ctx.close();
            return;
        }
        byte[] body = msg.getBody();
        if (body == null || body.length == 0) {
            log.error("消息体为空, 关闭连接, imMsg:{}", msg);
            return;
        }
        sendMsgToMq(body);
    }

    private void sendMsgToMq(byte[] body) {
        ImMsgBody imMsgBody = JSON.parseObject(body, ImMsgBody.class);
        Message message = new Message();
        message.setTopic(ImCoreServerTopicNameConstant.LIVE_IM_BIZ_MSG_TOPIC);
        message.setBody(JSON.toJSONBytes(imMsgBody));
        try {
            SendResult result = mqProducer.send(message);
            log.info("【BizImMsgHandler】, 消息投递结果, imMsgBody:{}, result:{}", imMsgBody, result);
        } catch (Exception e) {
            log.error("【BizImMsgHandler】, 消息发送失败, imMsgBody:{}", imMsgBody, e);
            throw new RuntimeException(e);
        }
    }
}
