package top.flobby.live.gift.provider.rpc;

import com.alibaba.fastjson2.JSON;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.common.message.Message;
import org.springframework.transaction.annotation.Transactional;
import top.flobby.live.bank.constant.OrderStatusEnum;
import top.flobby.live.bank.interfaces.ICurrencyAccountRpc;
import top.flobby.live.common.utils.ConvertBeanUtils;
import top.flobby.live.gift.dto.PrepareOrderReqDTO;
import top.flobby.live.gift.dto.RollBackStockDTO;
import top.flobby.live.gift.dto.ShopCarReqDTO;
import top.flobby.live.gift.dto.SkuOrderInfoDTO;
import top.flobby.live.gift.interfaces.ISkuOrderInfoRPC;
import top.flobby.live.gift.provider.dao.po.SkuInfoPO;
import top.flobby.live.gift.provider.dao.po.SkuOrderInfoPO;
import top.flobby.live.gift.provider.service.IShopCarService;
import top.flobby.live.gift.provider.service.ISkuInfoService;
import top.flobby.live.gift.provider.service.ISkuOrderInfoService;
import top.flobby.live.gift.provider.service.ISkuStockInfoService;
import top.flobby.live.gift.vo.ShopCarItemRespVO;
import top.flobby.live.gift.vo.ShopCarRespVO;
import top.flobby.live.gift.vo.SkuOrderInfoVO;
import top.flobby.live.gift.vo.SkuPrepareOrderVO;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static top.flobby.live.common.constants.GiftProviderTopicNamesConstant.ROLL_BACK_STOCK;

/**
 * @author : Flobby
 * @program : live-api
 * @description :
 * @create : 2024-03-11 11:55
 **/

@Slf4j
@DubboService
public class SkuOrderInfoRpcImpl implements ISkuOrderInfoRPC {
    @Resource
    private ISkuOrderInfoService skuOrderInfoService;
    @Resource
    private IShopCarService shopCarService;
    @Resource
    private ISkuStockInfoService skuStockInfoService;
    @Resource
    private ISkuInfoService skuInfoService;
    @Resource
    private MQProducer mqProducer;
    @DubboReference
    private ICurrencyAccountRpc accountRpc;

    @Override
    public SkuOrderInfoVO queryLastByUserIdAndRoomId(Long userId, Integer roomId) {
        return skuOrderInfoService.queryLastByUserIdAndRoomId(userId, roomId);
    }

    @Override
    public boolean insertOne(SkuOrderInfoDTO skuOrderInfoDTO) {
        return skuOrderInfoService.insertOne(skuOrderInfoDTO) != null;
    }

    @Override
    public boolean updateStatus(Integer orderId, Integer status) {
        return skuOrderInfoService.updateStatus(orderId, status);
    }

    @Override
    public SkuPrepareOrderVO prepareOrder(PrepareOrderReqDTO prepareOrderReqDTO) {
        // 解析购物车信息，获取商品和数量
        ShopCarReqDTO cartReq = ConvertBeanUtils.convert(prepareOrderReqDTO, ShopCarReqDTO.class);
        ShopCarRespVO carInfo = shopCarService.getCarInfo(cartReq);
        if (carInfo == null) {
            return null;
        }
        List<ShopCarItemRespVO> cartItemInfoList = carInfo.getShopCarItemRespVOS();
        if (cartItemInfoList == null || cartItemInfoList.isEmpty()) {
            return null;
        }
        List<Long> skuIds = cartItemInfoList.stream().map(item -> item.getSkuInfoDTO().getSkuId()).collect(Collectors.toList());
        // 先检查库存是否充足,并扣减库存
        boolean flag = skuStockInfoService.decrStockNumBySkuIdInBatch(skuIds, 1);
        if (!flag) {
            return null;
        }
        // 核心内容，库存回滚
        SkuOrderInfoDTO skuOrderInfoDto = new SkuOrderInfoDTO();
        skuOrderInfoDto.setSkuIds(skuIds);
        skuOrderInfoDto.setStatus(OrderStatusEnum.WAIT_PAY.getCode().intValue());
        skuOrderInfoDto.setUserId(prepareOrderReqDTO.getUserId());
        skuOrderInfoDto.setRoomId(prepareOrderReqDTO.getRoomId());
        // 生成订单后，删除购物车中的商品
        SkuOrderInfoPO orderPo = skuOrderInfoService.insertOne(skuOrderInfoDto);
        shopCarService.clearShopCar(cartReq);
        // 订单超时业务，例如21:00下单，21:30未支付，自动取消订单
        // 1. 定时任务，扫描数据库表，找出超时的订单，取消订单，如果数据量过大，会有性能问题
        // 2. redis的过期回调，key过期后，会触发回调。但是回调并不是高可靠的，可能会丢失
        // 3. rocketMQ的延迟消息,时间轮，最合适的方案
        // 将扣减的库存信息，利用rmq发送，然后利用延迟回调回滚库存
        stockRollBackHandler(prepareOrderReqDTO.getUserId(), orderPo.getId());
        SkuPrepareOrderVO resultVo = new SkuPrepareOrderVO();
        resultVo.setOrderId(orderPo.getId());
        resultVo.setCarItemRespList(cartItemInfoList);
        resultVo.setTotalPrice(cartItemInfoList.stream().mapToInt(item -> item.getSkuInfoDTO().getSkuPrice()).sum());
        return resultVo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean payNow(Integer orderId, Long userId) {
        SkuOrderInfoVO orderInfo = skuOrderInfoService.queryByOrderId(orderId);
        if (orderInfo == null || !orderInfo.getStatus().equals(OrderStatusEnum.WAIT_PAY.getCode().intValue())) {
            return false;
        }
        List<Long> skuIdList = Arrays.asList(orderInfo.getSkuIdList().split(",")).stream().map(Long::parseLong).collect(Collectors.toList());
        if (skuIdList.isEmpty()) {
            return false;
        }
        int totalPrice = skuInfoService.queryBySkuIds(skuIdList).stream().mapToInt(SkuInfoPO::getSkuPrice).sum();
        Integer userBalance = accountRpc.getUserBalance(userId);
        if (userBalance < totalPrice) {
            return false;
        }
        boolean payFlag = accountRpc.decrement(userId, totalPrice);
        if (!payFlag) {
            return false;
        }
        return skuOrderInfoService.updateStatus(orderId, OrderStatusEnum.PAY_SUCCESS.getCode().intValue());
    }

    /**
     * 库存回滚处理机
     * 半小时的延迟消息
     *
     * @param userId  用户 ID
     * @param orderId 订单编号
     */
    private void stockRollBackHandler(Long userId, Integer orderId) {
        Message message = new Message();
        message.setTopic(ROLL_BACK_STOCK);
        RollBackStockDTO msgBody = new RollBackStockDTO();
        msgBody.setOrderId(orderId);
        msgBody.setUserId(userId);
        message.setBody(JSON.toJSONBytes(msgBody));
        // 1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h
        message.setDelayTimeLevel(16);
        try {
            mqProducer.send(message);
            log.info("[SkuOrderInfoRpcImpl] 库存回滚消息发送成功");
        } catch (Exception e) {
            log.error("[SkuOrderInfoRpcImpl] 库存回滚消息发送失败", e);
        }
    }
}
