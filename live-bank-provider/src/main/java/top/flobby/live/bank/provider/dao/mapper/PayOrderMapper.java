package top.flobby.live.bank.provider.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.flobby.live.bank.provider.dao.po.PayOrderPO;

/**
 * @author : Flobby
 * @program : live-api
 * @description :
 * @create : 2023-12-20 10:10
 **/

@Mapper
public interface PayOrderMapper extends BaseMapper<PayOrderPO> {
}
