package top.flobby.live.bank.provider.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.springframework.stereotype.Service;
import top.flobby.live.bank.constant.OrderStatusEnum;
import top.flobby.live.bank.constant.PayProductTypeEnum;
import top.flobby.live.bank.dto.PayOrderDTO;
import top.flobby.live.bank.dto.PayProductDTO;
import top.flobby.live.bank.provider.dao.mapper.PayOrderMapper;
import top.flobby.live.bank.provider.dao.po.PayOrderPO;
import top.flobby.live.bank.provider.dao.po.PayTopicPO;
import top.flobby.live.bank.provider.service.*;
import top.flobby.live.common.utils.ConvertBeanUtils;

import java.util.UUID;

/**
 * @author : Flobby
 * @program : live-api
 * @description :
 * @create : 2023-12-20 10:13
 **/

@Slf4j
@Service
public class PayOrderServiceImpl implements IPayOrderService {

    @Resource
    private PayOrderMapper payOrderMapper;
    @Resource
    private IPayProductService payProductService;
    @Resource
    private IPayTopicService payTopicService;
    @Resource
    private ICurrencyAccountService currencyAccountService;
    @Resource
    private ICurrencyTradeService currencyTradeService;
    @Resource
    private MQProducer mqProducer;

    @Override
    public PayOrderPO queryByOrderId(String orderId) {
        LambdaQueryWrapper<PayOrderPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PayOrderPO::getOrderId, orderId);
        return payOrderMapper.selectOne(wrapper);
    }

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

    @Override
    public boolean payNotify(PayOrderDTO payOrderDTO) {
        // 支付成功后，业务案例举例
        // 需要发送消息通知 -> msg-provider
        // 修改用户会员经验 -> user-provider
        PayOrderPO payOrder = queryByOrderId(payOrderDTO.getOrderId());
        if (payOrder == null) {
            log.error("[PayOrderServiceImpl] 未找到对应的订单，orderId={}", payOrderDTO.getOrderId());
            return false;
        }
        PayTopicPO payTopic = payTopicService.queryByBizCode(payOrderDTO.getBizCode());
        if (payTopic == null || StringUtils.isBlank(payTopic.getTopic())) {
            log.error("[PayOrderServiceImpl] 未找到对应的topic，bizCode={}", payOrderDTO.getBizCode());
            return false;
        }
        // 修改用户余额，修改订单状态
        this.payNotifyHandler(payOrder);
        // 发送MQ，进行消费
        // 中台服务，对接方很多，会导致很多topic出现，所以使用bizCode进行管理
        // 为了方面扩展，单独创建一个表，进行topic和bizCode的映射
        Message message = new Message();
        message.setTopic(payTopic.getTopic());
        message.setBody(JSON.toJSONBytes(payOrder));
        try {
            // TODO 消费者业务待实现
            SendResult result = mqProducer.send(message);
            log.info("[PayOrderServiceImpl] 发送MQ消息，{}", result);
        } catch (Exception e) {
            log.error("[PayOrderServiceImpl] 发送MQ消息失败，", e);
        }
        return true;
    }

    /**
     * 支付处理
     *
     * @param payOrder 支付订单
     */
    private void payNotifyHandler(PayOrderPO payOrder) {
        this.updateOrderStatus(payOrder.getOrderId(), OrderStatusEnum.PAY_SUCCESS.getCode());
        // 获取产品信息，增加用户余额
        PayProductDTO product = payProductService.getPayProductById(payOrder.getProductId());
        if (product == null || !PayProductTypeEnum.LIVING_ROOM_PRODUCT.getType().equals(product.getType())) {
            log.error("[PayOrderServiceImpl] 未找到对应的产品，productId={}", payOrder.getProductId());
            return;
        }
        Integer coin = JSON.parseObject(product.getExtra()).getInteger("coin");
        currencyAccountService.increment(payOrder.getUserId(), coin);
    }
}
