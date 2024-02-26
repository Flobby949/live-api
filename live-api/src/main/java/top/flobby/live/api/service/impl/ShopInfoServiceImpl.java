package top.flobby.live.api.service.impl;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import top.flobby.live.api.dto.SkuInfoReqDTO;
import top.flobby.live.api.service.IShopInfoService;
import top.flobby.live.api.vo.SkuDetailInfoVO;
import top.flobby.live.api.vo.SkuInfoVO;
import top.flobby.live.common.utils.ConvertBeanUtils;
import top.flobby.live.gift.dto.SkuInfoDTO;
import top.flobby.live.gift.interfaces.ISkuInfoRPC;
import top.flobby.live.living.interfaces.ILivingRoomRpc;
import top.flobby.live.living.vo.LivingRoomInfoVO;

import java.util.Collections;
import java.util.List;

/**
 * @author : Flobby
 * @program : live-api
 * @description : impl
 * @create : 2024-02-26 13:35
 **/

@Service
public class ShopInfoServiceImpl implements IShopInfoService {

    @DubboReference
    private ILivingRoomRpc livingRoomRpc;
    @DubboReference
    private ISkuInfoRPC skuInfoRpc;

    @Override
    public List<SkuInfoVO> queryByRoomId(Integer roomId) {
        LivingRoomInfoVO livingRoomInfoVO = livingRoomRpc.queryLivingRoomByRoomId(roomId);
        if (ObjectUtils.isEmpty(livingRoomInfoVO)) {
            return Collections.emptyList();
        }
        Long anchorId = livingRoomInfoVO.getAnchorId();
        List<SkuInfoDTO> skuInfoDTOS = skuInfoRpc.queryByAnchorId(anchorId);
        if (CollectionUtils.isEmpty(skuInfoDTOS)) {
            return Collections.emptyList();
        }
        return ConvertBeanUtils.convertList(skuInfoDTOS, SkuInfoVO.class);
    }

    @Override
    public SkuDetailInfoVO detail(SkuInfoReqDTO skuInfoReqDTO) {
        return ConvertBeanUtils.convert(skuInfoRpc.queryBySkuId(skuInfoReqDTO.getSkuId()), SkuDetailInfoVO.class);
    }
}
