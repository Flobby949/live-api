package top.flobby.live.user.provider.dao.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 用户登录账号实体
 * @create : 2023-12-03 14:26
 **/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_user_phone")
public class UserPhonePO {

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 用户 ID
     */
    private Long userId;
    /**
     * 电话
     */
    private String phone;
    /**
     * 状态
     * 0 - 无效
     * 1 - 有效
     */
    private Byte status;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
}
