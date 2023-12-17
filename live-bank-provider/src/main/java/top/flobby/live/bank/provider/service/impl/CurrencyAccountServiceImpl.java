package top.flobby.live.bank.provider.service.impl;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import top.flobby.live.bank.dto.AccountTradeDTO;
import top.flobby.live.bank.dto.CurrencyAccountDTO;
import top.flobby.live.bank.provider.dao.mapper.CurrencyAccountMapper;
import top.flobby.live.bank.provider.dao.po.CurrencyAccountPO;
import top.flobby.live.bank.provider.service.ICurrencyAccountService;
import top.flobby.live.bank.vo.AccountTradeVO;
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

    @Override
    public AccountTradeVO consume(AccountTradeDTO accountTradeDTO) {
        long userId = accountTradeDTO.getUserId();
        int tradeAmount = accountTradeDTO.getTradeAmount();
        CurrencyAccountDTO userAccount = getByUserId(userId);
        // 首先考虑账户异常的情况
        if (ObjectUtils.isEmpty(userAccount) || !CommonStatusEnum.VALID.getCode().equals(userAccount.getStatus())) {
            return AccountTradeVO.buildFailure(userId, "用户账户异常");
        }
        // 其次考虑余额不足的情况
        if (userAccount.getCurrentBalance() < tradeAmount) {
            return AccountTradeVO.buildFailure(userId, "用户余额不足");
        }
        // 扣除余额
        decrement(userId, tradeAmount);
        // TODO 插入交易记录
        // TODO 性能问题
        return AccountTradeVO.buildSuccess(userId);
    }
}
