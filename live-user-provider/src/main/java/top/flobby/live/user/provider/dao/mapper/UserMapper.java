package top.flobby.live.user.provider.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.flobby.live.user.provider.dao.po.UserPO;

/**
 * @author : Flobby
 * @program : live-api
 * @description : mapper
 * @create : 2023-11-18 14:43
 **/

@Mapper
public interface UserMapper extends BaseMapper<UserPO> {
}
