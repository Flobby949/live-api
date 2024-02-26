package top.flobby.live.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : Flobby
 * @program : live-api
 * @description : sku请求dto
 * @create : 2024-02-26 13:34
 **/

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SkuInfoReqDTO {
    private Long skuId;
}
