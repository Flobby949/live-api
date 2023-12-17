package top.flobby.live.bank.provider.service.impl;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import top.flobby.live.bank.dto.CurrencyAccountDTO;
import top.flobby.live.bank.provider.dao.mapper.CurrencyAccountMapper;
import top.flobby.live.bank.provider.dao.po.CurrencyAccountPO;
import top.flobby.live.bank.provider.service.ICurrencyAccountService;
import top.flobby.live.common.enums.CommonStatusEnum;
import top.flobby.live.common.utils.ConvertBeanUtils;

import java.util.Date;

/**
 * @author : Flobby
 * @program : live-api
 * @description :
 * @create : 2023-12-17 15:26
 **/

@Service
public class CurrencyAccountServiceImpl implements ICurrencyAccountService {

    @Resource
    private CurrencyAccountMapper currencyAccountMapper;

    @Override
    public boolean insertOneAccount(Long userId) {
        CurrencyAccountPO accountPO = CurrencyAccountPO.builder().userId(userId)
                .currentBalance(0)
                .totalCharged(0)
                .status(CommonStatusEnum.VALID.getCode())
                .createTime(new Date())
                .updateTime(new Date())
                .build();
        try {
            currencyAccountMapper.insert(accountPO);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void increment(Long userId, int num) {
        currencyAccountMapper.increment(userId, num);
    }

    @Override
    public void decrement(Long userId, int num) {
        currencyAccountMapper.decrement(userId, num);
    }

    @Override
    public CurrencyAccountDTO getByUserId(Long userId) {
        CurrencyAccountPO accountPO = currencyAccountMapper.selectById(userId);
        return ConvertBeanUtils.convert(accountPO, CurrencyAccountDTO.class);
    }
}
