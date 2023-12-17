package top.flobby.live.gift.provider.service.impl;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import top.flobby.live.common.utils.ConvertBeanUtils;
import top.flobby.live.gift.dto.GiftRecordDTO;
import top.flobby.live.gift.provider.dao.mapper.GiftRecordMapper;
import top.flobby.live.gift.provider.dao.po.GiftRecordPO;
import top.flobby.live.gift.provider.service.IGiftRecordService;

/**
 * @author : Flobby
 * @program : live-api
 * @description :
 * @create : 2023-12-17 13:13
 **/

@Service
public class GiftRecordServiceImpl implements IGiftRecordService {

    @Resource
    private GiftRecordMapper giftRecordMapper;

    @Override
    public void addGiftRecord(GiftRecordDTO giftRecordDTO) {
        GiftRecordPO recordPO = ConvertBeanUtils.convert(giftRecordDTO, GiftRecordPO.class);
        giftRecordMapper.insert(recordPO);
    }
}
