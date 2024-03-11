package top.flobby.live.api.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import top.flobby.live.api.dto.ShopCarReq;
import top.flobby.live.api.dto.SkuInfoReqDTO;
import top.flobby.live.api.service.IShopInfoService;
import top.flobby.live.api.vo.SkuDetailInfoVO;
import top.flobby.live.api.vo.SkuInfoVO;
import top.flobby.live.common.resp.CommonResp;
import top.flobby.live.gift.vo.ShopCarRespVO;
import top.flobby.live.web.starter.context.RequestContext;

import java.util.List;

@RestController
@RequestMapping("/shop")
public class ShopInfoController {

    @Resource
    private IShopInfoService shopInfoService;

    @GetMapping("/listSkuInfo")
    public CommonResp<List<SkuInfoVO>> list(@RequestParam Integer roomId) {
        return CommonResp.success(shopInfoService.queryByRoomId(roomId));
    }

    @GetMapping("/detail")
    public CommonResp<SkuDetailInfoVO> detail(@RequestParam SkuInfoReqDTO dto) {
        return CommonResp.success(shopInfoService.detail(dto));
    }

    // 用户进入直播间，查看到商品列表
    // 用户查看商品详情
    // 用户把感兴趣的商品，加入待支付的购物车中（购物车的概念）-> 购物车的基本存储结构（按照直播间为维度去设计购物车）
    // 直播间的购物车是独立的，不会存在数据跨直播间存在的情况
    // 购物车的添加，移除
    // 购物车的内容展示
    // 购物车的清空

    /**
     * 往购物车添加商品
     */
    @PostMapping("/addCar")
    public CommonResp<Boolean> addCart(@RequestBody ShopCarReq req) {
        return CommonResp.success(shopInfoService.addCar(req));
    }

    /**
     * 从购物车移除商品
     */
    @PostMapping("/removeFromCar")
    public CommonResp<Boolean> removeFromCart(ShopCarReq req) {
        return CommonResp.success(shopInfoService.removeFromCar(req));

    }

    /**
     * 查看购物车信息
     */
    @PostMapping("/getCarInfo")
    public CommonResp<ShopCarRespVO> getCartInfo(ShopCarReq req) {
        return CommonResp.success(shopInfoService.getCarInfo(req));
    }

    /**
     * 清空购物车信息
     */
    @PostMapping("/clearCar")
    public CommonResp<Boolean> clearCart(ShopCarReq req) {
        return CommonResp.success(shopInfoService.clearShopCar(req));
    }

    // 购物车以及塞满了，下边的逻辑是怎样的？
    // 预下单，（手机产品100台，库存的预锁定操作）
    // 如果下单成功（库存就正常扣减了）
    // 如果到达一定时间限制没有下单（100台手机，100台库存锁定，不支付，支付倒计时， 库存回滚，订单状态会变成支付超时状态）

    @PostMapping("/prepareOrder")
    public CommonResp<Boolean> prepareOrder(Integer roomId) {
        return CommonResp.success(shopInfoService.prepareOrder(RequestContext.getUserId(), roomId));
    }

}