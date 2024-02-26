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
 * @description : sku信息
 * @create : 2024-02-26 13:12
 **/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_sku_info")
public class SkuInfoPO {
    /**
     * CREATE TABLE `t_sku_info` (
     * `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
     * `sku_id` int unsigned NOT NULL DEFAULT '0' COMMENT 'sku id',
     * `sku_price` int unsigned NOT NULL DEFAULT '0' COMMENT 'sku价格',
     * `sku_code` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'sku编码',
     * `name` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '商品名称',
     * `icon_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '缩略图',
     * `original_icon_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '原图',
     * `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商品描述',
     * `status` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '状态(0下架，1上架)',
     * `category_id` int NOT NULL COMMENT '类目id',
     * `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
     * `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
     * PRIMARY KEY (`id`)
     * ) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品sku信息表';
     */

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long skuId;
    private Integer skuPrice;
    private String skuCode;
    private String name;
    private String iconUrl;
    private String originalIconUrl;
    private String remark;
    private Integer status;
    private Integer categoryId;
    private Date createTime;
    private Date updateTime;
}
