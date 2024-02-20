package top.flobby.live.gift.provider.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import top.flobby.live.bank.interfaces.ICurrencyAccountRpc;
import top.flobby.live.common.constants.GiftProviderTopicNamesConstant;
import top.flobby.live.common.utils.ListUtils;
import top.flobby.live.framework.redis.starter.key.GiftProviderCacheKeyBuilder;
import top.flobby.live.gift.constant.RedPacketStatusEnum;
import top.flobby.live.gift.dto.GetRedPacketDTO;
import top.flobby.live.gift.provider.dao.mapper.RedPacketConfigMapper;
import top.flobby.live.gift.provider.dao.po.RedPacketConfigPO;
import top.flobby.live.gift.provider.service.IRedPacketService;
import top.flobby.live.gift.provider.service.bo.SendRedPacketBO;
import top.flobby.live.gift.vo.RedPacketReceiveVO;
import top.flobby.live.im.common.AppIdEnum;
import top.flobby.live.im.dto.ImMsgBody;
import top.flobby.live.im.router.constants.ImMsgBizCodeEnum;
import top.flobby.live.im.router.interfaces.ImRouterRpc;
import top.flobby.live.living.dto.LivingRoomReqDTO;
import top.flobby.live.living.interfaces.ILivingRoomRpc;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author : Flobby
 * @program : live-api
 * @description :
 * @create : 2024-01-07 13:20
 **/

@Slf4j
@Service
public class RedPacketServiceImpl implements IRedPacketService {
    @Resource
    private RedPacketConfigMapper redPacketConfigMapper;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private GiftProviderCacheKeyBuilder cacheKeyBuilder;
    @DubboReference
    private ImRouterRpc routerRpc;
    @DubboReference
    private ILivingRoomRpc livingRoomRpc;
    @Resource
    private MQProducer mqProducer;
    @DubboReference
    private ICurrencyAccountRpc currencyAccountRpc;

    @Override
    public RedPacketConfigPO queryRedPacketConfigByAnchorId(Long anchorId) {
        LambdaQueryWrapper<RedPacketConfigPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RedPacketConfigPO::getAnchorId, anchorId).eq(RedPacketConfigPO::getStatus, RedPacketStatusEnum.WAITING.getCode()).orderByDesc(RedPacketConfigPO::getCreateTime);
        return redPacketConfigMapper.selectOne(wrapper);
    }

    @Override
    public RedPacketConfigPO queryPreparedByConfigCode(String configCode) {
        LambdaQueryWrapper<RedPacketConfigPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RedPacketConfigPO::getConfigCode, configCode).eq(RedPacketConfigPO::getStatus, RedPacketStatusEnum.PREPARED.getCode()).orderByDesc(RedPacketConfigPO::getCreateTime);
        return redPacketConfigMapper.selectOne(wrapper);
    }

    @Override
    public boolean addRedPacketConfig(RedPacketConfigPO redPacketConfigPO) {
        redPacketConfigPO.setConfigCode(UUID.randomUUID().toString());
        return redPacketConfigMapper.insert(redPacketConfigPO) > 0;
    }

    @Override
    public boolean updateRedPacketByAnchorId(RedPacketConfigPO redPacketConfigPO) {
        return redPacketConfigMapper.updateById(redPacketConfigPO) > 0;
    }

    @Override
    public boolean prepareRedPacket(Long anchorId) {
        RedPacketConfigPO redPacketConfig = this.queryRedPacketConfigByAnchorId(anchorId);
        if (redPacketConfig == null) {
            // 防止重复生成或者空数据
            return false;
        }
        // 加锁
        String configCode = redPacketConfig.getConfigCode();
        String lockKey = cacheKeyBuilder.buildRedPacketListInitLock(configCode);
        Boolean lock = redisTemplate.opsForValue().setIfAbsent(lockKey, 1, 3, TimeUnit.SECONDS);
        if (lock == null || !lock) {
            // 加锁失败
            return false;
        }
        Integer totalCount = redPacketConfig.getTotalCount();
        Integer totalPrice = redPacketConfig.getTotalPrice();
        // 生成数据
        List<Integer> redPacketBalanceList = this.createRedPacketBalanceList(totalCount, totalPrice);
        String cacheKey = cacheKeyBuilder.buildRedPacketListKey(configCode);
        // 拆分list，分批次存入redis
        ListUtils.spiltList(redPacketBalanceList, 100).forEach(list ->
                redisTemplate.opsForList().leftPushAll(cacheKey, list.toArray()));
        redisTemplate.expire(cacheKey, 1, TimeUnit.DAYS);
        // 更新状态
        redPacketConfig.setStatus(RedPacketStatusEnum.PREPARED.getCode());
        this.updateRedPacketByAnchorId(redPacketConfig);
        // 记录初始化完成
        String initDoneKey = cacheKeyBuilder.buildRedPacketListPrepareDone(configCode);
        redisTemplate.opsForValue().set(initDoneKey, 1, 1, TimeUnit.DAYS);
        return true;
    }

    /**
     * 创建红包
     * 使用两倍均值法生成红包金额列表
     *
     * @param totalCount 总数
     * @param totalPrice 总价
     * @return {@link List}<{@link Integer}>
     */
    private List<Integer> createRedPacketBalanceList(Integer totalCount, Integer totalPrice) {
        List<Integer> redPacketBalanceList = new ArrayList<>(totalCount);
        for (int i = 0; i < totalCount; i++) {
            if (i == totalCount - 1) {
                // 最后一个红包,直接放入剩余金额
                redPacketBalanceList.add(totalPrice);
                break;
            }
            // 生成随机金额
            int maxLimit = totalPrice / (totalCount - i) * 2;
            int currentPrice = ThreadLocalRandom.current().nextInt(1, maxLimit);
            totalPrice -= currentPrice;
            redPacketBalanceList.add(currentPrice);
        }
        return redPacketBalanceList;
    }

    @Override
    public RedPacketReceiveVO receiveRedPacket(GetRedPacketDTO getRedPacketDTO) {
        String configCode = getRedPacketDTO.getConfigCode();
        String cacheKey = cacheKeyBuilder.buildRedPacketListKey(configCode);
        Object redPacketBalanceObj = redisTemplate.opsForList().rightPop(cacheKey);
        if (redPacketBalanceObj == null) {
            // 红包已经领完
            return null;
        }
        Integer redPacketBalance = (Integer) redPacketBalanceObj;
        log.info("[RedPacketServiceImpl] 红包领取,configCode:{},price:{}", configCode, redPacketBalance);
        SendRedPacketBO sendRedPacketBO = SendRedPacketBO.builder()
                .reqDTO(getRedPacketDTO)
                .price(redPacketBalance)
                .build();
        Message message = new Message();
        message.setTopic(GiftProviderTopicNamesConstant.RECEIVE_RED_PACKET);
        message.setBody(JSON.toJSONBytes(sendRedPacketBO));
        try {
            SendResult result = mqProducer.send(message);
            log.info("[RedPacketServiceImpl]  发送消息完毕 , result:{}", result);
            if (SendStatus.SEND_OK.equals(result.getSendStatus())) {
                return RedPacketReceiveVO.builder().price(redPacketBalance).status(true).build();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return RedPacketReceiveVO.builder().status(false).build();
    }

    @Override
    public boolean startRedPacket(GetRedPacketDTO getRedPacketDTO) {
        String configCode = getRedPacketDTO.getConfigCode();
        // 检查红包是否已经初始化完成
        if (Boolean.FALSE.equals(redisTemplate.hasKey(cacheKeyBuilder.buildRedPacketListPrepareDone(configCode)))) {
            return false;
        }
        // 检查红包是否已经启动（只能广播一次）
        String notifyKey = cacheKeyBuilder.buildRedPacketNotify(configCode);
        if (Boolean.TRUE.equals(redisTemplate.hasKey(notifyKey))) {
            return false;
        }
        // 开始广播
        RedPacketConfigPO redPacketConfig = this.queryPreparedByConfigCode(configCode);
        JSONObject msgDataBody = new JSONObject();
        msgDataBody.put("redPacketConfig", JSON.toJSONString(redPacketConfig));
        List<Long> userIdList = livingRoomRpc.queryUserIdByRoomId(LivingRoomReqDTO.builder()
                .id(getRedPacketDTO.getRoomId())
                .appId(AppIdEnum.LIVE_BIZ_ID.getCode())
                .build());
        if (CollectionUtils.isEmpty(userIdList)) {
            return false;
        }
        this.batchSendImMsg(userIdList, ImMsgBizCodeEnum.START_RED_PACKET.getCode(), msgDataBody);
        redisTemplate.opsForValue().set(notifyKey, 1, 1, TimeUnit.DAYS);
        // 更新状态
        redPacketConfig.setStatus(RedPacketStatusEnum.HAS_SENT.getCode());
        this.updateRedPacketByAnchorId(redPacketConfig);
        return true;
    }

    /**
     * 批量发送消息
     *
     * @param userIdList  用户 ID 列表
     * @param bizCode     代码
     * @param msgDataBody msg 数据正文
     */
    private void batchSendImMsg(List<Long> userIdList, Integer bizCode, JSONObject msgDataBody) {
        List<ImMsgBody> imMsgBodies = userIdList.stream().map(userId -> ImMsgBody.builder().appId(AppIdEnum.LIVE_BIZ_ID.getCode()).bizCode(bizCode).userId(userId).data(msgDataBody.toJSONString()).build()).collect(Collectors.toList());
        log.info("[LivingRoomServiceImpl] 发送消息 is {}", imMsgBodies.get(0));
        routerRpc.batchSendMsg(imMsgBodies);
    }

    @Override
    public void receiveRedPacketHandle(GetRedPacketDTO reqDTO, Integer price) {
        String configCode = reqDTO.getConfigCode();
        // 记录用户领取总金额
        String userTotalPrice = cacheKeyBuilder.buildRedPacketUserTotalPrice(configCode, reqDTO.getUserId());
        redisTemplate.opsForValue().increment(userTotalPrice, price);
        // 领取数量
        String totalGetCacheKey = cacheKeyBuilder.buildRedPacketTotalGetCache(configCode);
        redisTemplate.opsForValue().increment(totalGetCacheKey);
        redisTemplate.expire(totalGetCacheKey, 1, TimeUnit.DAYS);
        // 领取金额
        String totalGetPriceCacheKey = cacheKeyBuilder.buildRedPacketTotalGetPrice(configCode);
        redisTemplate.opsForValue().increment(totalGetPriceCacheKey, price);
        redisTemplate.expire(totalGetPriceCacheKey, 1, TimeUnit.DAYS);
        // 持久化数据
        redPacketConfigMapper.incrTotalGet(configCode);
        redPacketConfigMapper.incrTotalGetPrice(configCode, price);
        // 保存金额到用户账户
        currencyAccountRpc.increment(reqDTO.getUserId(), price);
        // TODO 红包领取最大值，lua脚本实现，避免并发问题
        // TODO 等主播下播时，再统一保存到数据库
    }
}
