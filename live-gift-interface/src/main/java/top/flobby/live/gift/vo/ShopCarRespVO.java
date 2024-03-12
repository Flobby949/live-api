package top.flobby.live.gift.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 商品购物车数据返回
 * @create : 2024-03-08 13:22
 **/

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShopCarRespVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 4275901815149913181L;

    private Long userId;
    private Integer roomId;
    private List<ShopCarItemRespVO> shopCarItemRespVOS;
}
