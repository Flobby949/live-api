package top.flobby.live.gift.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.util.Date;

/**
 * 红包配置PO
 *
 * @author Flobby
 * @date 2024/01/07
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RedPacketConfigDTO implements java.io.Serializable {

    @Serial
    private static final long serialVersionUID = -4825657525582288741L;
    
    /**
     * 主键ID。
     */
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
     * 一共领取金额。
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
