package top.flobby.live.api.service.impl;

import com.alibaba.fastjson2.JSON;
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
import top.flobby.live.bank.interfaces.ICurrencyAccountRpc;
import top.flobby.live.common.constants.GiftProviderTopicNamesConstant;
import top.flobby.live.common.exception.BusinessException;
import top.flobby.live.common.exception.BusinessExceptionEnum;
import top.flobby.live.common.utils.ConvertBeanUtils;
import top.flobby.live.gift.dto.GiftConfigDTO;
import top.flobby.live.gift.dto.SendGiftMqDTO;
import top.flobby.live.gift.interfaces.IGiftRpc;
import top.flobby.live.web.starter.context.RequestContext;

import java.util.List;

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
    @DubboReference
    private ICurrencyAccountRpc currencyAccountRpc;
    @Resource
    private MQProducer mqProducer;

    @Override
    public List<GiftConfigVO> listGift() {
        List<GiftConfigDTO> giftConfigDTOS = giftRpc.queryGiftList();
        return ConvertBeanUtils.convertList(giftConfigDTOS, GiftConfigVO.class);
    }

    @Override
    public boolean sendGift(GiftDTO giftDTO) {
        int giftId = giftDTO.getGiftId();
        GiftConfigDTO giftDtoById = giftRpc.getGiftById(giftId);
        if (ObjectUtils.isEmpty(giftDtoById)) {
            throw new BusinessException(BusinessExceptionEnum.SEND_GIFT_FAIL);
        }
        // MQ异步调用
        SendGiftMqDTO.builder()
                .giftId(giftId)
                .price(giftDtoById.getPrice())
                .receiverId(giftDTO.getReceiverId())
                .roomId(giftDTO.getRoomId())
                .userId(RequestContext.getUserId())
                .build();
        Message message = new Message();
        message.setTopic(GiftProviderTopicNamesConstant.SEND_GIFT);
        message.setBody(JSON.toJSONBytes(JSON.toJSONBytes(message)));
        try {
            SendResult result = mqProducer.send(message);
            log.info("[GiftServiceImpl] 发送消息成功，{}", result);
        } catch (Exception e) {
            log.error("[GiftServiceImpl] 发送消息失败，", e);
        }
        return true;
    }
}
