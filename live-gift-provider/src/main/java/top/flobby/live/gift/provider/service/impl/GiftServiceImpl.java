package top.flobby.live.gift.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import top.flobby.live.common.enums.CommonStatusEnum;
import top.flobby.live.common.utils.ConvertBeanUtils;
import top.flobby.live.gift.dto.GiftDTO;
import top.flobby.live.gift.provider.dao.mapper.GiftMapper;
import top.flobby.live.gift.provider.dao.po.GiftPO;
import top.flobby.live.gift.provider.service.IGiftService;

import java.util.Date;
import java.util.List;

/**
 * @author : Flobby
 * @program : live-api
 * @description :
 * @create : 2023-12-17 13:01
 **/

@Service
public class GiftServiceImpl implements IGiftService {

    @Resource
    private GiftMapper giftMapper;

    @Override
    public GiftDTO getGiftById(Integer giftId) {
        LambdaQueryWrapper<GiftPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GiftPO::getGiftId, giftId);
        wrapper.eq(GiftPO::getStatus, CommonStatusEnum.VALID.getCode());
        GiftPO giftPO = giftMapper.selectOne(wrapper);
        return ConvertBeanUtils.convert(giftPO, GiftDTO.class);
    }

    @Override
    public List<GiftDTO> queryGiftList() {
        LambdaQueryWrapper<GiftPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GiftPO::getStatus, CommonStatusEnum.VALID.getCode());
        List<GiftPO> giftPOList = giftMapper.selectList(wrapper);
        return ConvertBeanUtils.convertList(giftPOList, GiftDTO.class);
    }

    @Override
    public void insertOne(GiftDTO giftDTO) {
        GiftPO giftPO = ConvertBeanUtils.convert(giftDTO, GiftPO.class);
        giftPO.setStatus(CommonStatusEnum.VALID.getCode());
        giftPO.setCreateTime(new Date());
        giftPO.setUpdateTime(new Date());
        giftMapper.insert(giftPO);
    }

    @Override
    public void updateOne(GiftDTO giftDTO) {
        GiftPO giftPO = ConvertBeanUtils.convert(giftDTO, GiftPO.class);
        giftMapper.updateById(giftPO);
    }
}
