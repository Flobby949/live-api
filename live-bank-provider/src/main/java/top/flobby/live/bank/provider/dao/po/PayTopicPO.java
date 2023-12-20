package top.flobby.live.bank.provider.dao.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author : Flobby
 * @program : live-api
 * @description : mq主题映射
 * @create : 2023-12-20 11:24
 **/

@Data
@TableName("t_pay_topic")
public class PayTopicPO {
    /**
     * CREATE TABLE `t_pay_topic` (
     * `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
     * `topic` varchar(255) NOT NULL COMMENT 'mq主题',
     * `biz_code` int NOT NULL COMMENT '业务code',
     * `status` tinyint NOT NULL COMMENT '状态',
     * `remark` varchar(255) NOT NULL COMMENT '描述',
     * `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建',
     * `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新',
     * PRIMARY KEY (`id`)
     * ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    private String topic;
    private Integer bizCode;
    private Byte status;
    private String remark;
    private Date createTime;
    private Date updateTime;
}
