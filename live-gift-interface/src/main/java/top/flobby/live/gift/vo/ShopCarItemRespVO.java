package top.flobby.live.gift.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import top.flobby.live.gift.dto.SkuInfoDTO;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author : Flobby
 * @program : live-api
 * @description :
 * @create : 2024-03-08 13:41
 **/

@Data
@AllArgsConstructor
public class ShopCarItemRespVO implements Serializable {
    @Serial
    private static final long serialVersionUID = -3053806506755468737L;
    private Integer count;
    private SkuInfoDTO skuInfoDTO;
}
