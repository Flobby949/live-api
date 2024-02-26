package top.flobby.live.gift.provider.dao.po;

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
 * @description : 类目表
 * @create : 2024-02-26 13:10
 **/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_category_info")
public class CategoryInfoPO {
    /**
     * CREATE TABLE `t_category_info` (
     * `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
     * `level` int unsigned NOT NULL DEFAULT '0' COMMENT '类目级别',
     * `parent_id` int unsigned NOT NULL DEFAULT '0' COMMENT '父类目id',
     * `category_name` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '类目名称',
     * `status` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '状态（0无效，1有效）',
     * `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
     * `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
     * PRIMARY KEY (`id`)
     * ) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='类目表';
     */

    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer level;
    private Integer parentId;
    private String categoryName;
    private Integer status;
    private Date createTime;
    private Date updateTime;
}
