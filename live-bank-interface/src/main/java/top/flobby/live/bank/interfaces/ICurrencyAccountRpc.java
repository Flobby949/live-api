package top.flobby.live.bank.interfaces;

import top.flobby.live.bank.dto.AccountTradeDTO;
import top.flobby.live.bank.vo.AccountTradeVO;

/**
 * @author : Flobby
 * @program : live-api
 * @description :
 * @create : 2023-12-17 15:25
 **/

public interface ICurrencyAccountRpc {

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
    boolean decrement(Long userId, int num);

    /**
     * 获取用户余额
     *
     * @param userId 用户 ID
     * @return {@link Integer}
     */
    Integer getUserBalance(Long userId);

    /**
     * 消费送礼
     *
     * @param accountTradeDTO 账户 交易 DTO
     * @return {@link AccountTradeVO}
     */
    AccountTradeVO consumeForSendGift(AccountTradeDTO accountTradeDTO);
}
