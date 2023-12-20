package top.flobby.live.bank.provider.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import top.flobby.live.bank.dto.PayProductDTO;
import top.flobby.live.bank.interfaces.IPayProductRpc;
import top.flobby.live.bank.provider.service.IPayProductService;

import java.util.List;

/**
 * @author : Flobby
 * @program : live-api
 * @description :
 * @create : 2023-12-20 08:30
 **/

@DubboService
public class PayProductRpcImpl implements IPayProductRpc {

    @Resource
    private IPayProductService payProductService;

    @Override
    public List<PayProductDTO> productList(Integer type) {
        return payProductService.productList(type);
    }

    @Override
    public PayProductDTO getPayProductById(Integer productId) {
        return payProductService.getPayProductById(productId);
    }
}
