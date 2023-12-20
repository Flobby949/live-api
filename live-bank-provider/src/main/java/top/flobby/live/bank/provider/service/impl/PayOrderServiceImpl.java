package top.flobby.live.bank.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import top.flobby.live.bank.dto.PayOrderDTO;
import top.flobby.live.bank.provider.dao.mapper.PayOrderMapper;
import top.flobby.live.bank.provider.dao.po.PayOrderPO;
import top.flobby.live.bank.provider.service.IPayOrderService;
import top.flobby.live.common.utils.ConvertBeanUtils;

import java.util.UUID;

/**
 * @author : Flobby
 * @program : live-api
 * @description :
 * @create : 2023-12-20 10:13
 **/

@Service
public class PayOrderServiceImpl implements IPayOrderService {

    @Resource
    private PayOrderMapper payOrderMapper;

    @Override
    public String insertOne(PayOrderDTO payOrderDTO) {
        PayOrderPO order = ConvertBeanUtils.convert(payOrderDTO, PayOrderPO.class);
        order.setOrderId(UUID.randomUUID().toString());
        payOrderMapper.insert(order);
        return order.getOrderId();
    }

    @Override
    public boolean updateOrderStatus(String orderId, Byte status) {
        LambdaQueryWrapper<PayOrderPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PayOrderPO::getOrderId, orderId);
        PayOrderPO order = payOrderMapper.selectOne(wrapper);
        if (order == null) {
            return false;
        }
        order.setStatus(status);
        return payOrderMapper.updateById(order) > 0;
    }

    @Override
    public boolean updateOrderStatus(Long id, Byte status) {
        LambdaQueryWrapper<PayOrderPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PayOrderPO::getId, id);
        PayOrderPO order = payOrderMapper.selectOne(wrapper);
        if (order == null) {
            return false;
        }
        order.setStatus(status);
        return payOrderMapper.updateById(order) > 0;
    }
}
