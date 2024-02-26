package top.flobby.live.api.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * SKU info vo
 *
 * @author Flobby
 * @date 2024/02/26
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SkuInfoVO {

    private Long skuId;
    private Integer skuPrice;
    private String skuCode;
    private String name;
    private String iconUrl;
    private String originalIconUrl;
    private String remark;
}