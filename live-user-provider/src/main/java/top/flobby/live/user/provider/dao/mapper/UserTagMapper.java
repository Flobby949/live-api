package top.flobby.live.user.provider.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;
import top.flobby.live.user.provider.dao.po.UserTagPO;

/**
 * @author : Flobby
 * @program : live-api
 * @description :
 * @create : 2023-11-28 12:00
 **/

@Mapper
public interface UserTagMapper extends BaseMapper<UserTagPO> {

    /**
     * 设置标签
     * 原标签和新标签进行或（ | ）运算
     * <p>
     * ${fieldName} & #{tag} = 0 用于判断是否不存在该标签
     *
     * @param userId    用户 ID
     * @param fieldName 字段名称
     * @param tag       标记
     * @return int
     */
    @Update("update t_user_tag set ${fieldName} = ${fieldName} | #{tag} where user_id = #{userId} and ${fieldName} & #{tag} = 0 ")
    int setTag(Long userId, String fieldName, Long tag);

    /**
     * 删除标签
     * 原标签和新标签取反（  ~ ）后进行与（  & ）运算
     * <p>
     * ${fieldName} & #{tag} = #{tag} 用于判断是否存在该标签
     *
     * @param userId    用户 ID
     * @param fieldName 字段名称
     * @param tag       标记
     * @return int
     */
    @Update("update t_user_tag set ${fieldName} = ${fieldName} &~ #{tag} where user_id = #{userId} and ${fieldName} & #{tag} = #{tag} ")
    int removeTag(Long userId, String fieldName, Long tag);
}
