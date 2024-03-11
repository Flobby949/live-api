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
 * @description : 商品订单表
 * @create : 2024-02-26 13:17
 **/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_sku_order_info")
public class SkuOrderInfoPO {
    /**
     * CREATE TABLE `t_sku_order_info` (
     * `id` int unsigned NOT NULL AUTO_INCREMENT,
     * `sku_id_list` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
     * `user_id` bigint unsigned NOT NULL DEFAULT '0' COMMENT '用户id',
     * `room_id` int unsigned NOT NULL DEFAULT '0' COMMENT '直播id',
     * `status` int unsigned NOT NULL DEFAULT '0' COMMENT '状态',
     * `extra` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
     * `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
     * `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
     * PRIMARY KEY (`id`)
     * ) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品订单表';
     */

    @TableId(type = IdType.AUTO)
    private Integer id;
    private String skuIdList;
    private Long userId;
    private Integer roomId;
    /**
     * 状态
     */
    private Integer status;
    private String extra;
    private Date createTime;
    private Date updateTime;
}
