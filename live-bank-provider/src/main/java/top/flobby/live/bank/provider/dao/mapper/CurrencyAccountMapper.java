package top.flobby.live.bank.provider.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import top.flobby.live.bank.provider.dao.po.CurrencyAccountPO;

/**
 * @author : Flobby
 * @program : live-api
 * @description :
 * @create : 2023-12-17 15:19
 **/

@Mapper
public interface CurrencyAccountMapper extends BaseMapper<CurrencyAccountPO> {

    /**
     * 增加
     * 不修改总充值数，余额变动有可能是通过打赏获得
     *
     * @param userId 用户 ID
     * @param num    数字
     */
    @Update("update t_currency_account set current_balance = current_balance + #{num}  where user_id = #{userId}")
    void increment(@Param("userId") Long userId, @Param("num") int num);


    /**
     * 扣减
     *
     * @param userId 用户 ID
     * @param num    数字
     */
    @Update("update t_currency_account set current_balance = current_balance - #{num}  where user_id = #{userId}")
    void decrement(@Param("userId") Long userId, @Param("num") int num);
}
