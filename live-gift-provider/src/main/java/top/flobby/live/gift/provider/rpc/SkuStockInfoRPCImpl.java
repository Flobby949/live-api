package top.flobby.live.gift.provider.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import top.flobby.live.gift.interfaces.ISkuStockInfoRPC;
import top.flobby.live.gift.provider.service.ISkuStockInfoService;
import top.flobby.live.gift.provider.service.bo.DcrStockNumBO;

/**
 * @author : Flobby
 * @program : live-api
 * @description :
 * @create : 2024-03-08 14:01
 **/

@DubboService
public class SkuStockInfoRPCImpl implements ISkuStockInfoRPC {
    @Resource
    private ISkuStockInfoService stockInfoService;

    private final int MAX_TRY_TIMES = 5;

    @Override
    public boolean dcrStockNumBySkuId(Long skuId, Integer num) {
        for (int i = 0; i < MAX_TRY_TIMES; i++) {
            DcrStockNumBO dcrStockNumBO = stockInfoService.dcrStockNumBySkuId(skuId, num);
            if (dcrStockNumBO.isNoStock()) {
                return false;
            } else if (dcrStockNumBO.isSuccess()) {
                return true;
            }
        }
        return false;
    }
}
