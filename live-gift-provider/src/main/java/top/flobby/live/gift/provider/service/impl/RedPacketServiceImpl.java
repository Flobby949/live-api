package top.flobby.live.gift.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import top.flobby.live.common.enums.CommonStatusEnum;
import top.flobby.live.gift.provider.dao.mapper.RedPacketConfigMapper;
import top.flobby.live.gift.provider.dao.po.RedPacketConfigPO;
import top.flobby.live.gift.provider.service.IRedPacketService;

/**
 * @author : Flobby
 * @program : live-api
 * @description :
 * @create : 2024-01-07 13:20
 **/

@Service
public class RedPacketServiceImpl implements IRedPacketService {
    @Resource
    private RedPacketConfigMapper redPacketConfigMapper;

    @Override
    public RedPacketConfigPO queryRedPacketConfigByAnchorId(Long anchorId) {
        LambdaQueryWrapper<RedPacketConfigPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RedPacketConfigPO::getAnchorId, anchorId)
                .eq(RedPacketConfigPO::getStatus, CommonStatusEnum.VALID.getCode())
                .orderByDesc(RedPacketConfigPO::getCreateTime);
        return redPacketConfigMapper.selectOne(wrapper);
    }

    @Override
    public boolean addRedPacketConfig(RedPacketConfigPO redPacketConfigPO) {
        return redPacketConfigMapper.insert(redPacketConfigPO) > 0;
    }

    @Override
    public boolean updateRedPacketByAnchorId(RedPacketConfigPO redPacketConfigPO) {
        return redPacketConfigMapper.updateById(redPacketConfigPO) > 0;
    }
}
