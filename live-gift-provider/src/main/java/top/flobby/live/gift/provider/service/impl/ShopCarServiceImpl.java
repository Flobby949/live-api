package top.flobby.live.gift.provider.service.impl;

import jakarta.annotation.Resource;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;
import top.flobby.live.common.utils.ConvertBeanUtils;
import top.flobby.live.framework.redis.starter.key.GiftProviderCacheKeyBuilder;
import top.flobby.live.gift.dto.ShopCarReqDTO;
import top.flobby.live.gift.dto.SkuInfoDTO;
import top.flobby.live.gift.provider.dao.po.SkuInfoPO;
import top.flobby.live.gift.provider.service.IShopCarService;
import top.flobby.live.gift.provider.service.ISkuInfoService;
import top.flobby.live.gift.vo.ShopCarItemRespVO;
import top.flobby.live.gift.vo.ShopCarRespVO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : Flobby
 * @program : live-api
 * @description :
 * @create : 2024-03-08 13:31
 **/

@Service
public class ShopCarServiceImpl implements IShopCarService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private GiftProviderCacheKeyBuilder cacheKeyBuilder;
    @Resource
    private ISkuInfoService skuInfoService;

    @Override
    public ShopCarRespVO getCarInfo(ShopCarReqDTO shopCarReqDTO) {
        String cacheKey = cacheKeyBuilder.buildUserShopCar(shopCarReqDTO.getUserId(), shopCarReqDTO.getRoomId());
        Cursor<Map.Entry<Object, Object>> allCarData = redisTemplate.opsForHash()
                .scan(cacheKey, ScanOptions.scanOptions().match("*").build());
        List<ShopCarItemRespVO> shopCarItemRespVoS = new ArrayList<>();
        Map<Long, Integer> skuCountMap = new HashMap<>();
        while (allCarData.hasNext()) {
            Map.Entry<Object, Object> entry = allCarData.next();
            skuCountMap.put(Long.parseLong(entry.getKey().toString()), (Integer) entry.getValue());
        }
        List<SkuInfoPO> skuInfoDTOList = skuInfoService.queryBySkuIds(new ArrayList<>(skuCountMap.keySet()));
        for (SkuInfoPO skuInfoPO : skuInfoDTOList) {
            SkuInfoDTO skuInfoDTO = ConvertBeanUtils.convert(skuInfoPO, SkuInfoDTO.class);
            Integer count = skuCountMap.get(skuInfoDTO.getSkuId());
            shopCarItemRespVoS.add(new ShopCarItemRespVO(count, skuInfoDTO));
        }
        ShopCarRespVO shopCarRespVO = new ShopCarRespVO();
        shopCarRespVO.setRoomId(shopCarReqDTO.getRoomId());
        shopCarRespVO.setUserId(shopCarReqDTO.getUserId());
        shopCarRespVO.setShopCarItemRespVOS(shopCarItemRespVoS);
        return shopCarRespVO;
    }

    @Override
    public Boolean addCar(ShopCarReqDTO shopCarReqDTO) {
        String cacheKey = cacheKeyBuilder.buildUserShopCar(shopCarReqDTO.getUserId(), shopCarReqDTO.getRoomId());
        // 一个用户 多个商品
        // 读取所有商品的数据
        // 每个商品都有数量（目前的业务场景中，没有体现）
        // string （对象，对象里面关联上商品的数据信息）
        // set / list
        // map （k,v） key是skuId，value是商品的数量,hIncr
        redisTemplate.opsForHash().put(cacheKey, shopCarReqDTO.getSkuId().toString(), 1);
        return true;
    }

    @Override
    public Boolean removeFromCar(ShopCarReqDTO shopCarReqDTO) {
        String cacheKey = cacheKeyBuilder.buildUserShopCar(shopCarReqDTO.getUserId(), shopCarReqDTO.getRoomId());
        redisTemplate.opsForHash().delete(cacheKey, shopCarReqDTO.getSkuId().toString());
        return true;
    }

    @Override
    public Boolean clearShopCar(ShopCarReqDTO shopCarReqDTO) {
        String cacheKey = cacheKeyBuilder.buildUserShopCar(shopCarReqDTO.getUserId(), shopCarReqDTO.getRoomId());
        redisTemplate.delete(cacheKey);
        return true;
    }


    @Override
    public Boolean addCarItemNum(ShopCarReqDTO shopCarReqDTO) {
        String cacheKey = cacheKeyBuilder.buildUserShopCar(shopCarReqDTO.getUserId(), shopCarReqDTO.getRoomId());
        redisTemplate.opsForHash().increment(cacheKey, shopCarReqDTO.getSkuId().toString(), 1);
        return true;
    }

    @Override
    public Boolean decrCarItemNum(ShopCarReqDTO shopCarReqDTO) {
        String cacheKey = cacheKeyBuilder.buildUserShopCar(shopCarReqDTO.getUserId(), shopCarReqDTO.getRoomId());
        redisTemplate.opsForHash().increment(cacheKey, shopCarReqDTO.getSkuId().toString(), -1);
        return true;
    }
}
