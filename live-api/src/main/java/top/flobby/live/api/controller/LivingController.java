package top.flobby.live.api.controller;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import top.flobby.live.api.dto.LivingRoomPkReqDTO;
import top.flobby.live.api.service.ILivingRoomService;
import top.flobby.live.common.resp.CommonResp;
import top.flobby.live.common.resp.PageRespVO;
import top.flobby.live.living.dto.LivingRoomPageDTO;
import top.flobby.live.living.vo.LivingRoomInfoVO;
import top.flobby.live.living.vo.LivingRoomInitVO;
import top.flobby.live.web.starter.context.RequestContext;
import top.flobby.live.web.starter.limit.RequestLimit;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 直播间相关接口
 * @create : 2023-12-12 10:44
 **/

@Slf4j
@RestController
@RequestMapping("/living")
public class LivingController {

    @Resource
    private ILivingRoomService livingRoomService;

    @PostMapping("start-living")
    @RequestLimit(limit = 1, second = 10)
    public CommonResp<Integer> startLiving(@RequestParam(name = "type", defaultValue = "0") Integer type) {
        log.info("开启直播，type={}", type);
        // 调用RPC，在开播表上写入一条记录
        Integer roomId = livingRoomService.startingLiving(type);
        if (ObjectUtils.isEmpty(roomId) || roomId <= 0) {
            return CommonResp.error("开启直播失败");
        }
        return CommonResp.success(roomId);
    }

    @PostMapping("close-living")
    @RequestLimit(limit = 1, second = 10)
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

    @PostMapping("query-page")
    public CommonResp<PageRespVO<LivingRoomInfoVO>> queryPage(@RequestBody LivingRoomPageDTO livingRoomPageDTO) {
        if (ObjectUtils.isEmpty(livingRoomPageDTO.getPage()) || livingRoomPageDTO.getPage() <= 0) {
            livingRoomPageDTO.setPage(1L);
        }
        if (ObjectUtils.isEmpty(livingRoomPageDTO.getPageSize()) || livingRoomPageDTO.getPageSize() <= 0) {
            livingRoomPageDTO.setPageSize(10L);
        }
        return CommonResp.success(livingRoomService.list(livingRoomPageDTO));
    }

    @PostMapping("online-pk")
    public CommonResp<Object> onlinePk(@RequestBody LivingRoomPkReqDTO livingRoomPkReqDTO) {
        if (ObjectUtils.isEmpty(livingRoomPkReqDTO)
                || ObjectUtils.isEmpty(livingRoomPkReqDTO.getRoomId())
                || livingRoomPkReqDTO.getRoomId() <= 0) {
            return CommonResp.error("房间号错误");
        }
        boolean result = livingRoomService.onlinePk(livingRoomPkReqDTO);
        if (!result) {
            return CommonResp.error("连线失败");
        }
        return CommonResp.success();
    }

    @PostMapping("offline-pk")
    public CommonResp<Object> offlinePk(@RequestBody LivingRoomPkReqDTO livingRoomPkReqDTO) {
        if (ObjectUtils.isEmpty(livingRoomPkReqDTO)
                || ObjectUtils.isEmpty(livingRoomPkReqDTO.getRoomId())
                || livingRoomPkReqDTO.getRoomId() <= 0) {
            return CommonResp.error("房间号错误");
        }
        boolean result = livingRoomService.offlinePk(livingRoomPkReqDTO);
        if (!result) {
            return CommonResp.error("断线失败");
        }
        return CommonResp.success();
    }
}
