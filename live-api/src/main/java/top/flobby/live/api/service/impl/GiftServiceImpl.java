package top.flobby.live.api.service.impl;

import com.alibaba.fastjson2.JSON;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import top.flobby.live.api.dto.GiftDTO;
import top.flobby.live.api.service.IGiftService;
import top.flobby.live.api.vo.GiftConfigVO;
import top.flobby.live.common.constants.GiftProviderTopicNamesConstant;
import top.flobby.live.common.exception.BusinessException;
import top.flobby.live.common.exception.BusinessExceptionEnum;
import top.flobby.live.common.utils.ConvertBeanUtils;
import top.flobby.live.gift.dto.GiftConfigDTO;
import top.flobby.live.gift.dto.SendGiftMqDTO;
import top.flobby.live.gift.interfaces.IGiftRpc;
import top.flobby.live.web.starter.context.RequestContext;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author : Flobby
 * @program : live-api
 * @description :
 * @create : 2023-12-17 16:55
 **/

@Slf4j
@Service
public class GiftServiceImpl implements IGiftService {

    @DubboReference
    private IGiftRpc giftRpc;
    @Resource
    private MQProducer mqProducer;

    private Cache<Integer, GiftConfigDTO> giftConfigDTOCache = Caffeine.newBuilder()
            .maximumSize(100)
            .expireAfterWrite(90, TimeUnit.SECONDS)
            .build();

    @Override
    public List<GiftConfigVO> listGift() {
        List<GiftConfigDTO> giftConfigDTOS = giftRpc.queryGiftList();
        return ConvertBeanUtils.convertList(giftConfigDTOS, GiftConfigVO.class);
    }

    @Override
    public boolean sendGift(GiftDTO giftDTO) {
        int giftId = giftDTO.getGiftId();
        // 从缓存中获取礼物信息,如果缓存中没有则RPC获取并放入缓存
        GiftConfigDTO giftDtoById = giftConfigDTOCache.get(giftId, k -> giftRpc.getGiftById(giftId));
        if (ObjectUtils.isEmpty(giftDtoById)) {
            throw new BusinessException(BusinessExceptionEnum.SEND_GIFT_FAIL);
        }
        // MQ异步调用
        SendGiftMqDTO body = SendGiftMqDTO.builder()
                .giftId(giftId)
                .price(giftDtoById.getPrice())
                .receiverId(giftDTO.getReceiverId())
                .roomId(giftDTO.getRoomId())
                .userId(RequestContext.getUserId())
                .svgaUrl(giftDtoById.getSvgaUrl())
                .type(giftDTO.getType())
                // 生成一个唯一的uuid，用来标识消息是否被消费过，避免重复消费
                .uuid(UUID.randomUUID().toString())
                .build();
        Message message = new Message();
        message.setTopic(GiftProviderTopicNamesConstant.SEND_GIFT);
        message.setBody(JSON.toJSONBytes(body));
        try {
            SendResult result = mqProducer.send(message);
            log.info("[GiftServiceImpl] 发送消息成功，{}", result);
        } catch (Exception e) {
            log.error("[GiftServiceImpl] 发送消息失败，", e);
        }
        return true;
    }
}
