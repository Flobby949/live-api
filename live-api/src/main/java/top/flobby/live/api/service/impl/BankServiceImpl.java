package top.flobby.live.api.service.impl;

import com.alibaba.fastjson2.JSON;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import top.flobby.live.api.dto.ProductReqDTO;
import top.flobby.live.api.service.IBankService;
import top.flobby.live.api.vo.PayProductItemVO;
import top.flobby.live.api.vo.PayProductVO;
import top.flobby.live.api.vo.ProductRespVO;
import top.flobby.live.bank.constant.OrderStatusEnum;
import top.flobby.live.bank.constant.PayChannelEnum;
import top.flobby.live.bank.constant.PaySourceEnum;
import top.flobby.live.bank.dto.PayOrderDTO;
import top.flobby.live.bank.dto.PayProductDTO;
import top.flobby.live.bank.interfaces.ICurrencyAccountRpc;
import top.flobby.live.bank.interfaces.IPayOrderRpc;
import top.flobby.live.bank.interfaces.IPayProductRpc;
import top.flobby.live.common.exception.BusinessException;
import top.flobby.live.common.exception.BusinessExceptionEnum;
import top.flobby.live.web.starter.context.RequestContext;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : Flobby
 * @program : live-api
 * @description :
 * @create : 2023-12-20 08:39
 **/

@Service
public class BankServiceImpl implements IBankService {

    @DubboReference
    private IPayProductRpc payProductRpc;
    @DubboReference
    private ICurrencyAccountRpc currencyAccountRpc;
    @DubboReference
    private IPayOrderRpc payOrderRpc;

    @Override
    public PayProductVO payProductList(Integer type) {
        List<PayProductItemVO> productList = payProductRpc.productList(type).stream().map(item -> {
            PayProductItemVO payProductItemVO = new PayProductItemVO();
            payProductItemVO.setId(item.getId());
            payProductItemVO.setName(item.getName());
            payProductItemVO.setCoinNum(JSON.parseObject(item.getExtra()).getInteger("coin"));
            return payProductItemVO;
        }).collect(Collectors.toList());
        PayProductVO payProductVO = new PayProductVO();
        payProductVO.setCurrentBalance(currencyAccountRpc.getUserBalance(RequestContext.getUserId()));
        payProductVO.setProductList(productList);
        return payProductVO;
    }

    @Override
    public ProductRespVO payProduct(ProductReqDTO productReqDTO) {
        // 参数校验
        if (productReqDTO == null
                || productReqDTO.getProductId() == null
                || productReqDTO.getPaySource() == null
                || PaySourceEnum.find(productReqDTO.getPaySource()) == null
                || PayChannelEnum.find(productReqDTO.getPayChannel()) == null) {
            throw new BusinessException(BusinessExceptionEnum.PARAMS_ERROR);
        }
        PayProductDTO product = payProductRpc.getPayProductById(productReqDTO.getProductId());
        if (product == null) {
            throw new BusinessException(BusinessExceptionEnum.PARAMS_ERROR);
        }
        // 插入订单，待支付
        PayOrderDTO payOrderDTO = new PayOrderDTO();
        payOrderDTO.setUserId(RequestContext.getUserId());
        payOrderDTO.setProductId(productReqDTO.getProductId());
        payOrderDTO.setSource(productReqDTO.getPaySource());
        payOrderDTO.setPayChannel(productReqDTO.getPayChannel());
        payOrderDTO.setStatus(OrderStatusEnum.WAIT_PAY.getCode());
        String orderId = payOrderRpc.insertOne(payOrderDTO);
        // 更新订单状态，支付中
        payOrderRpc.updateOrderStatus(orderId, OrderStatusEnum.PAYING.getCode());
        // 返回支付信息
        ProductRespVO productRespVO = new ProductRespVO();
        productRespVO.setOrderId(orderId);
        return productRespVO;
    }
}
