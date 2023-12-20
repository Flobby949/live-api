package top.flobby.live.bank.provider.service;

import top.flobby.live.bank.dto.PayOrderDTO;
import top.flobby.live.bank.provider.dao.po.PayOrderPO;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 支付
 * @create : 2023-12-20 10:13
 **/

public interface IPayOrderService {

    /**
     * 按订单 ID 查询
     *
     * @param orderId 订单编号
     * @return {@link PayOrderPO}
     */
    PayOrderPO queryByOrderId(String orderId);

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

    /**
     * 支付回调
     *
     * @param payOrderDTO 支付订单 DTO
     * @return boolean
     */
    boolean payNotify(PayOrderDTO payOrderDTO);
}
