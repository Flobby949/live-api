package top.flobby.live.gift.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import top.flobby.live.common.enums.CommonStatusEnum;
import top.flobby.live.gift.provider.dao.mapper.AnchorShopInfoMapper;
import top.flobby.live.gift.provider.dao.po.AnchorShopInfoPO;
import top.flobby.live.gift.provider.service.IAnchorShopInfoService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 主播店信息服务 impl
 *
 * @author Flobby
 * @date 2024/02/26
 */
@Service
public class AnchorShopInfoServiceImpl implements IAnchorShopInfoService {

    @Resource
    private AnchorShopInfoMapper anchorShopInfoMapper;

    @Override
    public List<Long> querySkuIdByAnchorId(Long anchorId) {
        LambdaQueryWrapper<AnchorShopInfoPO> qw = new LambdaQueryWrapper<>();
        qw.eq(AnchorShopInfoPO::getAnchorId, anchorId);
        qw.eq(AnchorShopInfoPO::getStatus, CommonStatusEnum.VALID.getCode());
        return anchorShopInfoMapper.selectList(qw).stream().map(AnchorShopInfoPO::getSkuId).collect(Collectors.toList());
    }

    @Override
    public List<Long> queryAllValidAnchorId() {
        LambdaQueryWrapper<AnchorShopInfoPO> qw = new LambdaQueryWrapper<>();
        qw.eq(AnchorShopInfoPO::getStatus, CommonStatusEnum.VALID.getCode());
        return anchorShopInfoMapper.selectList(qw).stream().map(AnchorShopInfoPO::getAnchorId).collect(Collectors.toList());
    }
}