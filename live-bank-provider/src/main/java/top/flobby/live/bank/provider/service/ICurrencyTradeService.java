package top.flobby.live.bank.provider.service;

/**
 * @author : Flobby
 * @program : live-api
 * @description :
 * @create : 2023-12-18 13:22
 **/

public interface ICurrencyTradeService {

    /**
     * 插入流水记录
     *
     * @param userId 用户 ID
     * @param num    金额
     * @param type   类型
     */
    boolean insertOne(Long userId, int num, int type);
}
