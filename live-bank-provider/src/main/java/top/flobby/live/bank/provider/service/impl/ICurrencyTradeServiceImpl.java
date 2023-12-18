package top.flobby.live.bank.provider.service.impl;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.flobby.live.bank.provider.dao.mapper.CurrencyTradeMapper;
import top.flobby.live.bank.provider.dao.po.CurrencyTradePO;
import top.flobby.live.bank.provider.service.ICurrencyTradeService;
import top.flobby.live.common.enums.CommonStatusEnum;

import java.util.Date;

/**
 * @author : Flobby
 * @program : live-api
 * @description :
 * @create : 2023-12-18 13:22
 **/

@Slf4j
@Service
public class ICurrencyTradeServiceImpl implements ICurrencyTradeService {

    @Resource
    private CurrencyTradeMapper currencyTradeMapper;

    @Override
    public boolean insertOne(Long userId, int num, int type) {
        try {
            int result = currencyTradeMapper.insert(CurrencyTradePO.builder()
                    .userId(userId)
                    .num(num)
                    .type((byte) type)
                    .status(CommonStatusEnum.VALID.getCode())
                    .createTime(new Date())
                    .updateTime(new Date())
                    .build());
            return result > 0;
        } catch (Exception e) {
            log.error("插入流水记录失败，用户 ID：{}，金额：{}，类型：{}", userId, num, type, e);
            return false;
        }
    }
}
