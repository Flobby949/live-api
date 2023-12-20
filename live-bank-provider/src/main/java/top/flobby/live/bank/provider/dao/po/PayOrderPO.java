package top.flobby.live.bank.provider.dao.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 支付订单
 * @create : 2023-12-20 10:08
 **/

@Data
@TableName("t_pay_order")
public class PayOrderPO {
    /**
     * CREATE TABLE `t_pay_order` (
     * `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
     * `order_id` varchar(60) NOT NULL COMMENT '订单编号',
     * `product_id` int NOT NULL COMMENT '产品id',
     * `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态（0待支付，1支付中，2已支付，3撤销，4无效）',
     * `user_id` bigint NOT NULL COMMENT '用户id',
     * `pay_channel` tinyint NOT NULL COMMENT '支付渠道（0支付宝，1微信，2银联，3收银台）',
     * `source` tinyint NOT NULL COMMENT '来源，看代码枚举',
     * `pay_time` datetime DEFAULT NULL COMMENT '支付时间',
     * `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
     * `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
     * PRIMARY KEY (`id`)
     * ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    private String orderId;
    private Integer productId;
    private Byte status;
    private Long userId;
    private Byte payChannel;
    /**
     * 来源
     *
     * @see top.flobby.live.bank.constant.PaySourceEnum
     */
    private Integer source;
    private String payTime;
    private Date createTime;
    private Date updateTime;
}
