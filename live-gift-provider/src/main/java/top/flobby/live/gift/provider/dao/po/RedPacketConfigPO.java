package top.flobby.live.gift.provider.dao.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * 红包配置PO
 *
 * @author Flobby
 * @date 2024/01/07
 */
@Builder
@Data
@TableName("t_red_packet_config")
public class RedPacketConfigPO {

    /**
     * 主键ID。
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 主播ID。
     */
    private Integer anchorId;

    /**
     * 红包雨活动开始时间。
     */
    private Date startTime;

    /**
     * 一共领取数量。
     */
    private Integer totalGet;

    /**
     * 红包活动结束后统计一共领取金额。
     */
    private Integer totalGetPrice;

    /**
     * 最大领取金额。
     */
    private Integer maxGetPrice;

    /**
     * 状态 (1 待准备，2 已准备，3 已发送)。
     */
    private Byte status;

    /**
     * 红包雨总金额数。
     */
    private Integer totalPrice;

    /**
     * 红包雨总红包数。
     */
    private Integer totalCount;

    /**
     * 唯一code。
     */
    private String configCode;

    /**
     * 备注。
     */
    private String remark;

    /**
     * 创建时间。
     */
    private Date createTime;

    /**
     * 更新时间。
     */
    private Date updateTime;

}
