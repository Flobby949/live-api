package top.flobby.live.api.service.impl;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import top.flobby.live.api.dto.GiftDTO;
import top.flobby.live.api.service.IGiftService;
import top.flobby.live.api.vo.GiftConfigVO;
import top.flobby.live.bank.dto.AccountTradeDTO;
import top.flobby.live.bank.interfaces.ICurrencyAccountRpc;
import top.flobby.live.bank.vo.AccountTradeVO;
import top.flobby.live.common.exception.BusinessException;
import top.flobby.live.common.exception.BusinessExceptionEnum;
import top.flobby.live.common.utils.ConvertBeanUtils;
import top.flobby.live.gift.dto.GiftConfigDTO;
import top.flobby.live.gift.interfaces.IGiftRpc;
import top.flobby.live.web.starter.context.RequestContext;

import java.util.List;

/**
 * @author : Flobby
 * @program : live-api
 * @description :
 * @create : 2023-12-17 16:55
 **/

@Service
public class GiftServiceImpl implements IGiftService {

    @DubboReference
    private IGiftRpc giftRpc;
    @DubboReference
    private ICurrencyAccountRpc currencyAccountRpc;

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
        // 同步调用，如果并发更大，可以改成MQ异步调用
        AccountTradeDTO accountTradeDTO = AccountTradeDTO.builder().userId(RequestContext.getUserId()).tradeAmount(giftDtoById.getPrice()).build();
        AccountTradeVO result = currencyAccountRpc.consumeForSendGift(accountTradeDTO);
        // 消费失败
        if (ObjectUtils.isEmpty(result) || !result.isOperationSuccess()) {
            throw new BusinessException(result.getMessage() + "，送礼失败");
        }
        return true;
    }
}
