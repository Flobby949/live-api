package top.flobby.live.api.controller;

import jakarta.annotation.Resource;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.flobby.live.api.service.ILivingRoomService;
import top.flobby.live.common.resp.CommonResp;
import top.flobby.live.living.vo.LivingRoomInitVO;
import top.flobby.live.web.starter.RequestContext;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 直播间相关接口
 * @create : 2023-12-12 10:44
 **/

@RestController
@RequestMapping("/living")
public class LivingController {

    @Resource
    private ILivingRoomService livingRoomService;

    @PostMapping("start-living")
    public CommonResp<Integer> startLiving(@RequestParam(name = "type", defaultValue = "0") Integer type) {
        // 调用RPC，在开播表上写入一条记录
        Integer roomId = livingRoomService.startingLiving(type);
        if (ObjectUtils.isEmpty(roomId) || roomId <= 0) {
            return CommonResp.error("开启直播失败");
        }
        return CommonResp.success(roomId);
    }

    @PostMapping("close-living")
    public CommonResp<Object> closeLiving(@RequestParam(name = "roomId") Integer roomId) {
        if (ObjectUtils.isEmpty(roomId) || roomId <= 0) {
            return CommonResp.error("房间号错误");
        }
        // 调用RPC，修改开播表的状态
        boolean result = livingRoomService.closeLiving(roomId);
        if (!result) {
            return CommonResp.error("关闭直播失败");
        }
        return CommonResp.success();
    }

    @PostMapping("anchor-config")
    public CommonResp<LivingRoomInitVO> anchorConfig(@RequestParam(name = "roomId") Integer roomId) {
        if (ObjectUtils.isEmpty(roomId) || roomId <= 0) {
            return CommonResp.error("房间号错误");
        }
        // 调用RPC，查询开播表的状态
        return CommonResp.success(livingRoomService.anchorConfig(RequestContext.getUserId(), roomId));
    }
}
