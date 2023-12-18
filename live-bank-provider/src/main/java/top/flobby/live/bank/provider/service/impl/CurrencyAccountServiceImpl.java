package top.flobby.live.bank.provider.service.impl;

import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import top.flobby.live.bank.constant.TradeTypeEnum;
import top.flobby.live.bank.dto.AccountTradeDTO;
import top.flobby.live.bank.dto.CurrencyAccountDTO;
import top.flobby.live.bank.provider.dao.mapper.CurrencyAccountMapper;
import top.flobby.live.bank.provider.dao.po.CurrencyAccountPO;
import top.flobby.live.bank.provider.service.ICurrencyAccountService;
import top.flobby.live.bank.provider.service.ICurrencyTradeService;
import top.flobby.live.bank.vo.AccountTradeVO;
import top.flobby.live.common.enums.CommonStatusEnum;
import top.flobby.live.common.utils.CommonUtils;
import top.flobby.live.common.utils.ConvertBeanUtils;
import top.flobby.live.framework.redis.starter.key.BankProviderCacheKeyBuilder;

import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private BankProviderCacheKeyBuilder cacheKeyBuilder;
    @Resource
    private ICurrencyTradeService currencyTradeService;

    public static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2, 4, 30, TimeUnit.SECONDS, new ArrayBlockingQueue<>(20));

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
        // 先更新redis的余额，再更新数据库的余额
        String cacheKey = cacheKeyBuilder.buildUserBalanceKey(userId);
        redisTemplate.opsForValue().decrement(cacheKey, num);
        // 异步线程池中操作DB，扣减余额，插入流水，带有事务
        // 分布式架构下 CAP 理论，本系统中优先可用性和性能，因此不保证强一致性，采用异步刷盘的方式
        threadPoolExecutor.execute(() -> consumeDbHandler(userId, num));
    }

    @Override
    public CurrencyAccountDTO getByUserId(Long userId) {
        CurrencyAccountPO accountPO = currencyAccountMapper.selectById(userId);
        return ConvertBeanUtils.convert(accountPO, CurrencyAccountDTO.class);
    }

    @Override
    public Integer getUserBalance(Long userId) {
        // 先查缓存
        String cacheKey = cacheKeyBuilder.buildUserBalanceKey(userId);
        Object cacheValue = redisTemplate.opsForValue().get(cacheKey);
        if (!ObjectUtils.isEmpty(cacheValue)) {
            Integer cacheResult = (Integer) cacheValue;
            if (cacheResult == -1) {
                return null;
            }
            return cacheResult;
        }
        // 缓存没有再查数据库
        Integer balance = currencyAccountMapper.queryCurrentBalanceByUserId(userId);
        if (ObjectUtils.isEmpty(balance)) {
            redisTemplate.opsForValue().set(cacheKey, -1, 5, TimeUnit.MINUTES);
            return null;
        }
        redisTemplate.opsForValue().set(cacheKey, balance, CommonUtils.createRandomExpireTime(), TimeUnit.SECONDS);
        return balance;
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

    @Override
    public AccountTradeVO consumeForSendGift(AccountTradeDTO accountTradeDTO) {
        // 余额判断
        long userId = accountTradeDTO.getUserId();
        int num = accountTradeDTO.getTradeAmount();
        Integer userBalance = getUserBalance(userId);
        if (ObjectUtils.isEmpty(userBalance) || userBalance < num) {
            return AccountTradeVO.buildFailure(userId, "用户余额不足");
        }
        // 业务处理
        this.decrement(userId, num);
        return AccountTradeVO.buildSuccess(userId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void consumeDbHandler(long userId, int num) {
        // 扣减余额
        currencyAccountMapper.decrement(userId, num);
        // 流水记录
        currencyTradeService.insertOne(userId, num, TradeTypeEnum.SEND_GIFT_TRADE.getCode());
    }
}
