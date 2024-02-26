package top.flobby.live.gift.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * SKU info DTO
 *
 * @author Flobby
 * @date 2024/02/26
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SkuInfoDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 7530341377552688847L;

    private Long id;
    private Long skuId;
    private Integer skuPrice;
    private String skuCode;
    private String name;
    private String iconUrl;
    private String originalIconUrl;
    private Integer status;
    private String remark;
    private Date createTime;
    private Date updateTime;
}