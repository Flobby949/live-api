package top.flobby.live.user.provider.dao.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * @description : 用户标签表
 * @create : 2023-11-28 11:57
 **/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_user_tag")
public class UserTagPO {
    /**
     * 用户 ID
     */
    @TableId(type = IdType.INPUT)
    private Long userId;
    /**
     * 标签 01
     */
    @TableField("tag_info_01")
    private Long tagInfo01;
    /**
     * 标签 02
     */
    @TableField("tag_info_02")
    private Long tagInfo02;
    /**
     * 标签 03
     */
    @TableField("tag_info_03")
    private Long tagInfo03;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
}
