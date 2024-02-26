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
 * @description : sku库存
 * @create : 2024-02-26 13:15
 **/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_sku_stock_info")
public class SkuStockInfoPO {
    /**
     * CREATE TABLE `t_sku_stock_info` (
     * `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
     * `sku_id` int unsigned NOT NULL DEFAULT '0' COMMENT 'sku id',
     * `stock_num` int unsigned NOT NULL DEFAULT '0' COMMENT 'sku库存',
     * `status` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '状态（0无效，1有效）',
     * `version` int unsigned DEFAULT NULL COMMENT '乐观锁',
     * `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
     * `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
     * PRIMARY KEY (`id`)
     * ) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='sku库存表';
     */

    @TableId(type = IdType.AUTO)
    private Integer id;
    private Long skuId;
    private Integer stockNum;
    private Integer status;
    private Integer version;
    private Date createTime;
    private Date updateTime;
}
