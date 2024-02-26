package top.flobby.live.gift.provider.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import top.flobby.live.common.utils.ConvertBeanUtils;
import top.flobby.live.gift.dto.SkuDetailInfoDTO;
import top.flobby.live.gift.dto.SkuInfoDTO;
import top.flobby.live.gift.interfaces.ISkuInfoRPC;
import top.flobby.live.gift.provider.dao.po.SkuInfoPO;
import top.flobby.live.gift.provider.service.IAnchorShopInfoService;
import top.flobby.live.gift.provider.service.ISkuInfoService;

import java.util.List;

/**
 * SKU info rpc impl
 *
 * @author Flobby
 * @date 2024/02/26
 */
@DubboService
public class SkuInfoRPCImpl implements ISkuInfoRPC {

    @Resource
    private ISkuInfoService skuInfoService;
    @Resource
    private IAnchorShopInfoService anchorShopInfoService;

    @Override
    public List<SkuInfoDTO> queryByAnchorId(Long anchorId) {
        List<Long> skuIdLIst = anchorShopInfoService.querySkuIdByAnchorId(anchorId);
        List<SkuInfoPO> skuInfoPOS = skuInfoService.queryBySkuIds(skuIdLIst);
        return ConvertBeanUtils.convertList(skuInfoPOS, SkuInfoDTO.class);
    }

    @Override
    public SkuDetailInfoDTO queryBySkuId(Long skuId) {
        SkuInfoPO skuInfoPO = skuInfoService.queryBySkuIdFromCache(skuId);
        return ConvertBeanUtils.convert(skuInfoPO, SkuDetailInfoDTO.class);
    }
}