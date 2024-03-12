package top.flobby.live.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author : Flobby
 * @program : live-api
 * @description :
 * @create : 2024-03-08 13:40
 **/

@Data
@AllArgsConstructor
public class ShopCarReq {
    private Long skuId;
    private Integer roomId;
}
