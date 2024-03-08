package top.flobby.live.gift.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import top.flobby.live.common.enums.CommonStatusEnum;
import top.flobby.live.gift.provider.dao.mapper.SkuStockInfoMapper;
import top.flobby.live.gift.provider.dao.po.SkuStockInfoPO;
import top.flobby.live.gift.provider.service.ISkuStockInfoService;
import top.flobby.live.gift.provider.service.bo.DcrStockNumBO;

/**
 * @author : Flobby
 * @program : live-api
 * @description :
 * @create : 2024-03-08 14:04
 **/

@Service
public class SkuStockInfoServiceImpl implements ISkuStockInfoService {
    @Resource
    private SkuStockInfoMapper skuStockInfoMapper;

    @Override
    public DcrStockNumBO dcrStockNumBySkuId(Long skuId, Integer num) {
        SkuStockInfoPO skuStockInfoPO = this.queryBySkuId(skuId);
        DcrStockNumBO dcrStockNumBO = new DcrStockNumBO();
        if (skuStockInfoPO.getStockNum() == 0 || skuStockInfoPO.getStockNum() - num < 0) {
            dcrStockNumBO.setNoStock(true);
            dcrStockNumBO.setSuccess(false);
            return dcrStockNumBO;
        }
        dcrStockNumBO.setNoStock(false);
        boolean updateState = skuStockInfoMapper.dcrStockNumBySkuId(skuId, num, skuStockInfoPO.getVersion()) > 0;
        dcrStockNumBO.setSuccess(updateState);
        return dcrStockNumBO;
    }

    @Override
    public SkuStockInfoPO queryBySkuId(Long skuId) {
        LambdaQueryWrapper<SkuStockInfoPO> qw = new LambdaQueryWrapper<>();
        qw.eq(SkuStockInfoPO::getSkuId, skuId)
                .eq(SkuStockInfoPO::getStatus, CommonStatusEnum.VALID.getCode());
        return skuStockInfoMapper.selectOne(qw);
    }
}
