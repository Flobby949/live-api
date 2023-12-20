package top.flobby.live.bank.provider.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import top.flobby.live.bank.dto.PayOrderDTO;
import top.flobby.live.bank.interfaces.IPayOrderRpc;
import top.flobby.live.bank.provider.service.IPayOrderService;

/**
 * @author : Flobby
 * @program : live-api
 * @description :
 * @create : 2023-12-20 10:25
 **/

@DubboService
public class PayOrderRpcImpl implements IPayOrderRpc {

    @Resource
    private IPayOrderService payOrderService;

    @Override
    public String insertOne(PayOrderDTO payOrderDTO) {
        return payOrderService.insertOne(payOrderDTO);
    }

    @Override
    public boolean updateOrderStatus(String orderId, Byte status) {
        return payOrderService.updateOrderStatus(orderId, status);
    }

    @Override
    public boolean updateOrderStatus(Long id, Byte status) {
        return payOrderService.updateOrderStatus(id, status);
    }

    @Override
    public boolean payNotify(PayOrderDTO payOrderDTO) {
        return payOrderService.payNotify(payOrderDTO);
    }
}
