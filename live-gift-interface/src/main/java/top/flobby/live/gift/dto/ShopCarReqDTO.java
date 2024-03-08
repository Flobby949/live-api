package top.flobby.live.gift.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 购物车请求DTO
 * @create : 2024-03-08 13:21
 **/

@Data
public class ShopCarReqDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1928455872630163623L;

    private Long userId;
    private Long skuId;
    private Integer roomId;
}
