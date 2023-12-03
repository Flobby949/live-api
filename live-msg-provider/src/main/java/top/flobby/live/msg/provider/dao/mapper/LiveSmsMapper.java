package top.flobby.live.msg.provider.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.flobby.live.msg.provider.dao.po.SmsPO;

/**
 * @author : Flobby
 * @program : live-api
 * @description : mapper
 * @create : 2023-12-03 13:02
 **/

@Mapper
public interface LiveSmsMapper extends BaseMapper<SmsPO> {
}
