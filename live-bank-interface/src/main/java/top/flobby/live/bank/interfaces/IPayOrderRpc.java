package top.flobby.live.bank.interfaces;

import top.flobby.live.bank.dto.PayOrderDTO;

/**
 * @author : Flobby
 * @program : live-api
 * @description :
 * @create : 2023-12-20 10:25
 **/

public interface IPayOrderRpc {

    /**
     * 插入
     *
     * @param payOrderDTO 支付订单 DTO
     * @return String OrderId
     */
    String insertOne(PayOrderDTO payOrderDTO);

    /**
     * 更新订单状态
     *
     * @param orderId 订单编号
     * @param status  状态
     * @return boolean
     */
    boolean updateOrderStatus(String orderId, Byte status);

    /**
     * 更新订单状态
     *
     * @param status 状态
     * @param id     编号
     * @return boolean
     */
    boolean updateOrderStatus(Long id, Byte status);
}
