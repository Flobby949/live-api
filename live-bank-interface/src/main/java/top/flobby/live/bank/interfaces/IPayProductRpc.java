package top.flobby.live.bank.interfaces;

import top.flobby.live.bank.dto.PayProductDTO;

import java.util.List;

/**
 * @author : Flobby
 * @program : live-api
 * @description :
 * @create : 2023-12-20 08:30
 **/

public interface IPayProductRpc {

    /**
     * 产品列表
     *
     * @param type 类型
     * @return {@link List}<{@link PayProductDTO}>
     */
    List<PayProductDTO> productList(Integer type);

    /**
     * 按 ID 获取支付产品
     *
     * @param productId 产品 ID
     * @return {@link PayProductDTO}
     */
    PayProductDTO getPayProductById(Integer productId);
}
