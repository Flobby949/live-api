package top.flobby.live.im.core.server.service.impl;

import com.alibaba.fastjson2.JSON;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import top.flobby.live.framework.redis.starter.key.ImCoreServerCacheKeyBuilder;
import top.flobby.live.im.core.server.service.IMsgAckCheckService;
import top.flobby.live.im.dto.ImMsgBody;

import static top.flobby.live.common.constants.ImCoreServerTopicNameConstant.LIVE_IM_ACK_MSG_TOPIC;

/**
 * @author : Flobby
 * @program : live-api
 * @description :
 * @create : 2023-12-09 14:05
 **/

@Slf4j
@Service
public class MsgAckCheckServiceImpl implements IMsgAckCheckService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private ImCoreServerCacheKeyBuilder cacheKeyBuilder;
    @Resource
    private MQProducer mqProducer;


    @Override
    public void doMsgCheck(ImMsgBody msgBody) {
        String key = cacheKeyBuilder.buildImAckMsgMapKey(msgBody.getUserId(), msgBody.getAppId());
        redisTemplate.opsForHash().delete(key, msgBody.getMsgId());
    }

    @Override
    public void recordAckMsg(ImMsgBody msgBody, Integer times) {
        String key = cacheKeyBuilder.buildImAckMsgMapKey(msgBody.getUserId(), msgBody.getAppId());
        redisTemplate.opsForHash().put(key, msgBody.getMsgId(), times);
    }

    @Override
    public void sendDelayMsg(ImMsgBody msgBody) {
        String bodyJson = JSON.toJSONString(msgBody);
        Message message = new Message();
        message.setTopic(LIVE_IM_ACK_MSG_TOPIC);
        message.setBody(bodyJson.getBytes());
        // 延迟等级为2，即延迟5s; PS:等级1 -> 1s
        message.setDelayTimeLevel(2);
        try {
            SendResult result = mqProducer.send(message);
            log.info("发送延迟消息成功, msg: {}, result: {}", bodyJson, result);
        } catch (Exception e) {
            log.error("发送延迟消息失败, " + e);
        }
    }

    @Override
    public int getAckMsgTimes(ImMsgBody msgBody) {
        Long userId = msgBody.getUserId();
        Integer appId = msgBody.getAppId();
        String msgId = msgBody.getMsgId();
        Object value = redisTemplate.opsForHash().get(cacheKeyBuilder.buildImAckMsgMapKey(userId, appId), msgId);
        if (ObjectUtils.isEmpty(value)) {
            return -1;
        }
        return (int) value;
    }
}
