package top.flobby.live.api.dto;

import lombok.Data;

/**
 * @author : Flobby
 * @program : live-api
 * @description :
 * @create : 2024-03-08 13:40
 **/

@Data
public class ShopCarReq {
    private Long skuId;
    private Integer roomId;
}
