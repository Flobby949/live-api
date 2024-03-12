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
    boolean decrement(Long userId, int num);

    /**
     * 按用户 ID 查询
     *
     * @param userId 用户 ID
     * @return {@link CurrencyAccountDTO}
     */
    CurrencyAccountDTO getByUserId(Long userId);

    /**
     * 获取用户余额
     *
     * @param userId 用户 ID
     * @return {@link Integer}
     */
    Integer getUserBalance(Long userId);

    /**
     * 消费
     * 底层判断用户余额是否足够，足够则扣除余额，不足则返回错误
     *
     * @param accountTradeDTO 账户 交易 DTO
     * @return {@link AccountTradeVO}
     */
    AccountTradeVO consume(AccountTradeDTO accountTradeDTO);


    /**
     * 消费送礼
     * 消费送礼接口
     * <p>
     * 大并发请求场景下，大量的用户同时消费，Mysql扛不住
     * 1. 把MySQL换成写入性能更好的数据库
     * 2. 从业务上进行优化，用户都连上了IM服务器，IM服务器进行扩容，并且使用MQ进行异步削峰，消费端也可以水平扩容
     * 客户端送礼时，接口同步校验（校验余额，存入redis），发送MQ，在异步操作中完成二次余额校验，扣除余额，发送礼物消息
     * 接口防重复点击，前端做好防重复点击
     * 余额不足，利用IM，反向通知发送方
     *
     * @param accountTradeDTO DTO
     * @return {@link AccountTradeVO}
     */
    AccountTradeVO consumeForSendGift(AccountTradeDTO accountTradeDTO);
}
