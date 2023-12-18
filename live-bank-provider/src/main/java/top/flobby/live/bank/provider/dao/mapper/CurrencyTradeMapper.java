package top.flobby.live.bank.provider.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.flobby.live.bank.provider.dao.po.CurrencyTradePO;

/**
 * @author : Flobby
 * @program : live-api
 * @description :
 * @create : 2023-12-18 13:21
 **/

@Mapper
public interface CurrencyTradeMapper extends BaseMapper<CurrencyTradePO> {
}
