package top.flobby.live.living.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import org.apache.ibatis.annotations.Options;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import top.flobby.live.common.enums.CommonStatusEnum;
import top.flobby.live.common.utils.ConvertBeanUtils;
import top.flobby.live.living.dto.LivingRoomReqDTO;
import top.flobby.live.living.provider.dao.mapper.LivingRoomMapper;
import top.flobby.live.living.provider.dao.mapper.LivingRoomRecordMapper;
import top.flobby.live.living.provider.dao.po.LivingRoomPO;
import top.flobby.live.living.provider.dao.po.LivingRoomRecordPO;
import top.flobby.live.living.provider.service.ILivingRoomService;
import top.flobby.live.living.vo.LivingRoomInfoVO;

import java.util.Date;

/**
 * @author : Flobby
 * @program : live-api
 * @description : impl
 * @create : 2023-12-12 09:55
 **/

@Service
public class LivingRoomServiceImpl implements ILivingRoomService {

    @Resource
    private LivingRoomMapper livingRoomMapper;
    @Resource
    private LivingRoomRecordMapper livingRoomRecordMapper;


    @Override
    public LivingRoomInfoVO queryByRoomId(Integer roomId) {
        LambdaQueryWrapper<LivingRoomPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LivingRoomPO::getId, roomId);
        wrapper.eq(LivingRoomPO::getStatus, CommonStatusEnum.VALID.getCode());
        LivingRoomPO livingRoom = livingRoomMapper.selectOne(wrapper);
        return ConvertBeanUtils.convert(livingRoom, LivingRoomInfoVO.class);
    }

    @Override
    @Options(useGeneratedKeys = true, keyProperty = "id")
    public Integer startLivingRoom(LivingRoomReqDTO livingRoomReqDTO) {
        LivingRoomPO livingRoomPO = ConvertBeanUtils.convert(livingRoomReqDTO, LivingRoomPO.class);
        livingRoomPO.setStatus(CommonStatusEnum.VALID.getCode());
        livingRoomPO.setStartTime(new Date());
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
        return true;
    }
}
