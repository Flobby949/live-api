package top.flobby.live.gift.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 预下单返回vo
 * @create : 2024-03-12 14:56
 **/

@Data
public class SkuPrepareOrderVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 6928893298016553884L;

    private List<ShopCarItemRespVO> carItemRespList;
    private Integer totalPrice;
    private Integer orderId;
}
