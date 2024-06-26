package top.flobby.live.bank.provider.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import top.flobby.live.bank.dto.AccountTradeDTO;
import top.flobby.live.bank.interfaces.ICurrencyAccountRpc;
import top.flobby.live.bank.provider.service.ICurrencyAccountService;
import top.flobby.live.bank.vo.AccountTradeVO;

/**
 * @author : Flobby
 * @program : live-api
 * @description :
 * @create : 2023-12-17 15:25
 **/

@DubboService
public class CurrencyAccountRpcImpl implements ICurrencyAccountRpc {

    @Resource
    private ICurrencyAccountService currencyAccountService;

    @Override
    public boolean insertOneAccount(Long userId) {
        return currencyAccountService.insertOneAccount(userId);
    }

    @Override
    public void increment(Long userId, int num) {
        currencyAccountService.increment(userId, num);
    }

    @Override
    public boolean decrement(Long userId, int num) {
        if (getUserBalance(userId) < num) {
            return false;
        }
        return currencyAccountService.decrement(userId, num);
    }

    @Override
    public Integer getUserBalance(Long userId) {
        return currencyAccountService.getUserBalance(userId);
    }

    @Override
    public AccountTradeVO consumeForSendGift(AccountTradeDTO accountTradeDTO) {
        return currencyAccountService.consumeForSendGift(accountTradeDTO);
    }
}
