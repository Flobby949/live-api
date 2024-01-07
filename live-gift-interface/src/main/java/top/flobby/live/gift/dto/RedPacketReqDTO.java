package top.flobby.live.gift.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;

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
public class RedPacketReqDTO implements java.io.Serializable {

    @Serial
    private static final long serialVersionUID = 300306733978669183L;

    /**
     * 主播ID。
     */
    private Integer anchorId;


    /**
     * 总金额。
     */
    private Integer totalPrice;


    /**
     * 红包雨总红包数。
     */
    private Integer totalCount;

    /**
     * 状态
     */
    private Byte status;

    /**
     * 备注。
     */
    private String remark;

}
