package top.flobby.live.living.provider.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.ibatis.annotations.Options;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import top.flobby.live.common.enums.CommonStatusEnum;
import top.flobby.live.common.resp.PageRespVO;
import top.flobby.live.common.utils.CommonUtils;
import top.flobby.live.common.utils.ConvertBeanUtils;
import top.flobby.live.framework.redis.starter.key.LivingProviderCacheKeyBuilder;
import top.flobby.live.im.common.AppIdEnum;
import top.flobby.live.im.core.server.dto.ImOfflineDTO;
import top.flobby.live.im.core.server.dto.ImOnlineDTO;
import top.flobby.live.im.dto.ImMsgBody;
import top.flobby.live.im.router.constants.ImMsgBizCodeEnum;
import top.flobby.live.im.router.interfaces.ImRouterRpc;
import top.flobby.live.living.dto.LivingRoomPageDTO;
import top.flobby.live.living.dto.LivingRoomReqDTO;
import top.flobby.live.living.provider.dao.mapper.LivingRoomMapper;
import top.flobby.live.living.provider.dao.mapper.LivingRoomRecordMapper;
import top.flobby.live.living.provider.dao.po.LivingRoomPO;
import top.flobby.live.living.provider.dao.po.LivingRoomRecordPO;
import top.flobby.live.living.provider.service.ILivingRoomService;
import top.flobby.live.living.vo.LivingRoomInfoVO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author : Flobby
 * @program : live-api
 * @description : impl
 * @create : 2023-12-12 09:55
 **/

@Slf4j
@Service
public class LivingRoomServiceImpl implements ILivingRoomService {

    @Resource
    private LivingRoomMapper livingRoomMapper;
    @Resource
    private LivingRoomRecordMapper livingRoomRecordMapper;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private LivingProviderCacheKeyBuilder cacheKeyBuilder;
    @DubboReference
    private ImRouterRpc imRouterRpc;

    @Override
    public LivingRoomInfoVO queryLivingRoomByRoomId(Integer roomId) {
        // 先查缓存
        String cacheKey = cacheKeyBuilder.buildLivingRoomObjKey(roomId);
        LivingRoomInfoVO queryResult = (LivingRoomInfoVO) redisTemplate.opsForValue().get(cacheKey);
        if (!ObjectUtils.isEmpty(queryResult)) {
            // 空值缓存
            if (queryResult.getId() == null) {
                return null;
            }
            return queryResult;
        }
        LambdaQueryWrapper<LivingRoomPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LivingRoomPO::getId, roomId);
        wrapper.eq(LivingRoomPO::getStatus, CommonStatusEnum.VALID.getCode());
        LivingRoomPO livingRoom = livingRoomMapper.selectOne(wrapper);
        queryResult = ConvertBeanUtils.convert(livingRoom, LivingRoomInfoVO.class);
        if (ObjectUtils.isEmpty(queryResult)) {
            // 防止缓存击穿
            redisTemplate.opsForValue().set(cacheKey, new LivingRoomInfoVO(), 1, TimeUnit.MINUTES);
            return null;
        }
        redisTemplate.opsForValue().set(cacheKey, queryResult, CommonUtils.createRandomExpireTime(), TimeUnit.SECONDS);
        return queryResult;
    }

    @Override
    @Options(useGeneratedKeys = true, keyProperty = "id")
    public Integer startLivingRoom(LivingRoomReqDTO livingRoomReqDTO) {
        LivingRoomPO livingRoomPO = ConvertBeanUtils.convert(livingRoomReqDTO, LivingRoomPO.class);
        livingRoomPO.setStatus(CommonStatusEnum.VALID.getCode());
        livingRoomPO.setStartTime(new Date());
        log.info("开启直播，livingRoomPO={}", livingRoomPO);
        livingRoomMapper.insert(livingRoomPO);
        return livingRoomPO.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean closeLiving(LivingRoomReqDTO livingRoomReqDTO) {
        LivingRoomPO livingRoomPO = livingRoomMapper.selectById(livingRoomReqDTO.getId());
        if (ObjectUtils.isEmpty(livingRoomPO)) {
            return false;
        }
        if (!livingRoomPO.getAnchorId().equals(livingRoomReqDTO.getAnchorId())) {
            return false;
        }
        LivingRoomRecordPO record = ConvertBeanUtils.convert(livingRoomPO, LivingRoomRecordPO.class);
        record.setEndTime(new Date());
        record.setStatus(CommonStatusEnum.INVALID.getCode());
        record.setUpdateTime(new Date());
        livingRoomRecordMapper.insert(record);
        livingRoomMapper.deleteById(livingRoomPO.getId());
        // 清除缓存
        String cacheKey = cacheKeyBuilder.buildLivingRoomObjKey(livingRoomPO.getId());
        redisTemplate.delete(cacheKey);
        return true;
    }

    @Override
    public PageRespVO<LivingRoomInfoVO> queryByPage(LivingRoomPageDTO livingRoomPageDTO) {
        // 先查缓存
        String cacheKey = cacheKeyBuilder.buildLivingRoomListKey(livingRoomPageDTO.getType());
        Long page = livingRoomPageDTO.getPage();
        Long pageSize = livingRoomPageDTO.getPageSize();
        long total = redisTemplate.opsForList().size(cacheKey);
        List<Object> resultList = redisTemplate.opsForList().range(cacheKey, (page - 1) * pageSize, page * pageSize - 1);
        PageRespVO<LivingRoomInfoVO> pageRespVO = new PageRespVO<>();
        if (CollectionUtils.isEmpty(resultList)) {
            pageRespVO.setList(Collections.emptyList());
            pageRespVO.setHasNext(false);
            return pageRespVO;
        }
        List<LivingRoomInfoVO> result = ConvertBeanUtils.convertList(resultList, LivingRoomInfoVO.class);
        pageRespVO.setList(result);
        pageRespVO.setHasNext(page * pageSize < total);
        return pageRespVO;
    }

    @Override
    public List<LivingRoomInfoVO> queryAllListFromDb(Integer type) {
        LambdaQueryWrapper<LivingRoomPO> wrapper = new LambdaQueryWrapper<>();
        // 如果类型存在就按类型查询
        wrapper.eq(!ObjectUtils.isEmpty(type), LivingRoomPO::getType, type);
        wrapper.eq(LivingRoomPO::getStatus, CommonStatusEnum.VALID.getCode());
        // 时间倒序
        wrapper.orderByDesc(LivingRoomPO::getId);
        wrapper.last("limit 1000");
        List<LivingRoomPO> result = livingRoomMapper.selectList(wrapper);
        return ConvertBeanUtils.convertList(result, LivingRoomInfoVO.class);
    }

    @Override
    public void userOnlineHandler(ImOnlineDTO imOnlineDTO) {
        Long userId = imOnlineDTO.getUserId();
        Integer roomId = imOnlineDTO.getRoomId();
        Integer appId = imOnlineDTO.getAppId();
        // 存入set
        String cacheKey = cacheKeyBuilder.buildLivingRoomUserKey(roomId, appId);
        redisTemplate.opsForSet().add(cacheKey, userId);
        redisTemplate.expire(cacheKey, 5, TimeUnit.HOURS);
    }

    @Override
    public void userOfflineHandler(ImOfflineDTO imOfflineDTO) {
        Long userId = imOfflineDTO.getUserId();
        Integer roomId = imOfflineDTO.getRoomId();
        Integer appId = imOfflineDTO.getAppId();
        String cacheKey = cacheKeyBuilder.buildLivingRoomUserKey(roomId, appId);
        redisTemplate.opsForSet().remove(cacheKey, userId);
        // PK直播下线
        LivingRoomReqDTO livingRoomReqDTO = new LivingRoomReqDTO();
        livingRoomReqDTO.setId(roomId);
        livingRoomReqDTO.setPkObjId(userId);
        this.offlinePk(livingRoomReqDTO);
    }

    @Override
    public List<Long> queryUserIdsByRoomId(LivingRoomReqDTO livingRoomReqDTO) {
        Integer roomId = livingRoomReqDTO.getId();
        Integer appId = livingRoomReqDTO.getAppId();
        String cacheKey = cacheKeyBuilder.buildLivingRoomUserKey(roomId, appId);
        // 没有全量查询，分页多段扫描
        Cursor<Object> cursor = redisTemplate.opsForSet().scan(cacheKey, ScanOptions.scanOptions().match("*").count(100).build());
        List<Long> userIdList = new ArrayList<>();
        while (cursor.hasNext()) {
            Integer userId = (Integer) cursor.next();
            userIdList.add(Long.valueOf(userId));
        }
        return userIdList;
    }

    @Override
    public boolean onlinePk(LivingRoomReqDTO livingRoomReqDTO) {
        String cacheKey = cacheKeyBuilder.buildLivingRoomOnlinePkKey(livingRoomReqDTO.getId());
        boolean tryOnline = redisTemplate.opsForValue().setIfAbsent(cacheKey, livingRoomReqDTO.getPkObjId(), CommonUtils.createRandomExpireTime() * 2, TimeUnit.SECONDS);
        if (!tryOnline) {
            return false;
        }
        List<Long> userIds = this.queryUserIdsByRoomId(livingRoomReqDTO);
        JSONObject msgData = new JSONObject();
        msgData.put("pkObjId", livingRoomReqDTO.getPkObjId());
        // TODO 如果有推拉流，那么还需要直播流的传输
        batchSendImMsg(userIds, ImMsgBizCodeEnum.LIVING_ROOM_PK_ONLINE.getCode(), msgData);
        return true;
    }

    @Override
    public boolean offlinePk(LivingRoomReqDTO livingRoomReqDTO) {
        String cacheKey = cacheKeyBuilder.buildLivingRoomOnlinePkKey(livingRoomReqDTO.getId());
        return Boolean.TRUE.equals(redisTemplate.delete(cacheKey));
    }

    @Override
    public Long queryOnlinePkUserId(Integer roomId) {
        String cacheKey = cacheKeyBuilder.buildLivingRoomOnlinePkKey(roomId);
        Object userId = redisTemplate.opsForValue().get(cacheKey);
        return userId != null ? (Long) userId : null;
    }

    /**
     * 批量发送 IM 消息
     *
     * @param userIds 用户 ID
     * @param bizCode 业务代码
     * @param msgData 数据
     */
    private void batchSendImMsg(List<Long> userIds, Integer bizCode, JSONObject msgData) {
        List<ImMsgBody> imMsgBodyList = userIds.stream().map(userId -> {
                    ImMsgBody imMsgBody = new ImMsgBody();
                    imMsgBody.setBizCode(bizCode);
                    imMsgBody.setData(msgData.toJSONString());
                    imMsgBody.setAppId(AppIdEnum.LIVE_BIZ_ID.getCode());
                    imMsgBody.setUserId(userId);
                    return imMsgBody;
                }
        ).collect(Collectors.toList());
        imRouterRpc.batchSendMsg(imMsgBodyList);
    }
}
