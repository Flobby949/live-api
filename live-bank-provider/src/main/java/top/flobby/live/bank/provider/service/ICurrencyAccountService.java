package top.flobby.live.bank.provider.service;

import top.flobby.live.bank.dto.CurrencyAccountDTO;

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
}
