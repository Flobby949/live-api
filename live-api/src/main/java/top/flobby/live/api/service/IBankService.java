package top.flobby.live.api.service;

import top.flobby.live.api.dto.ProductReqDTO;
import top.flobby.live.api.vo.PayProductVO;
import top.flobby.live.api.vo.ProductRespVO;

/**
 * @author : Flobby
 * @program : live-api
 * @description :
 * @create : 2023-12-20 08:39
 **/

public interface IBankService {
    /**
     * 支付产品列表
     *
     * @param type 类型
     * @return {@link PayProductVO}
     */
    PayProductVO payProductList(Integer type);

    ProductRespVO payProduct(ProductReqDTO productReqDTO);
}
