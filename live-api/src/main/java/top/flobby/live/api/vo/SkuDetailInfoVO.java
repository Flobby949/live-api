package top.flobby.live.api.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * SKU 详细信息 vo
 *
 * @author Flobby
 * @date 2024/02/26
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SkuDetailInfoVO {

    private Long skuId;
    private Integer skuPrice;
    private String skuCode;
    private String name;
    private String iconUrl;
    private String originalIconUrl;
    private String remark;
}