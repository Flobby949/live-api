package top.flobby.live.id.generate.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import top.flobby.live.id.generate.dao.po.IdGenerateConfigPO;

/**
 * @author : Flobby
 * @program : live-api
 * @description : mapper
 * @create : 2023-11-20 11:28
 **/

@Mapper
public interface IdGenerateMapper extends BaseMapper<IdGenerateConfigPO> {

    /**
     * 更新 ID 和版本
     * 告知数据库，当前已经使用到哪个值了
     * 分布式场景下 id 段抢占
     *
     * @param id      编号
     * @param version 版本
     * @return int
     */
    @Update("update t_id_generate_config set next_threshold = next_threshold + step, " +
            "current_start = current_start + step, version = version + 1 where id = #{id} and version = #{version} ")
    int updateNewIdAndVersion(@Param("id") int id, @Param("version") int version);
}
