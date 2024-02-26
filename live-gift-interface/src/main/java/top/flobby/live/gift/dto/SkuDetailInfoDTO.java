package top.flobby.live.gift.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * SKU 详细信息 DTO
 *
 * @author Flobby
 * @date 2024/02/26
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SkuDetailInfoDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 3642080561078376441L;

    private Long id;
    private Long skuId;
    private Integer skuPrice;
    private String skuCode;
    private String name;
    private String iconUrl;
    private String originalIconUrl;
    private Integer status;
    private String remark;
}