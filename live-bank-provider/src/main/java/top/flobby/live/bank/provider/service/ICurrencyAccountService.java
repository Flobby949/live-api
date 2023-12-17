package top.flobby.live.bank.provider.service;

import top.flobby.live.bank.dto.AccountTradeDTO;
import top.flobby.live.bank.dto.CurrencyAccountDTO;
import top.flobby.live.bank.vo.AccountTradeVO;

/**
 * @author : Flobby
 * @program : live-api
 * @description :
 * @create : 2023-12-17 15:20
 **/

public interface ICurrencyAccountService {

    /**
     * 插入一个帐户
     *
     * @param userId 用户 ID
     * @return boolean
     */
    boolean insertOneAccount(Long userId);

    /**
     * 增加
     *
     * @param userId 用户 ID
     * @param num    数字
     */
    void increment(Long userId, int num);

    /**
     * 扣减
     *
     * @param userId 用户 ID
     * @param num    数字
     */
    void decrement(Long userId, int num);

    /**
     * 按用户 ID 查询
     *
     * @param userId 用户 ID
     * @return {@link CurrencyAccountDTO}
     */
    CurrencyAccountDTO getByUserId(Long userId);

    /**
     * 消费
     * 底层判断用户余额是否足够，足够则扣除余额，不足则返回错误
     *
     * @param accountTradeDTO 账户 交易 DTO
     * @return {@link AccountTradeVO}
     */
    AccountTradeVO consume(AccountTradeDTO accountTradeDTO);
}
