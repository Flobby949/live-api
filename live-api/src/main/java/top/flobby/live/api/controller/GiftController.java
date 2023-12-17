package top.flobby.live.api.controller;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import top.flobby.live.api.dto.GiftDTO;
import top.flobby.live.api.service.IGiftService;
import top.flobby.live.api.vo.GiftConfigVO;
import top.flobby.live.common.resp.CommonResp;

import java.util.List;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 礼物模块接口
 * @create : 2023-12-17 16:51
 **/

@Slf4j
@RestController
@RequestMapping("/gift")
public class GiftController {

    @Resource
    private IGiftService giftService;

    @GetMapping("/list")
    public CommonResp<List<GiftConfigVO>> listGift() {
        // 调用方法，检索出来礼物配置列表
        List<GiftConfigVO> giftConfigVOS = giftService.listGift();
        return CommonResp.success(giftConfigVOS);
    }

    @PostMapping("/send")
    public CommonResp<Void> sendGift(@RequestBody GiftDTO giftDTO) {
        // 调用方法，送礼
        giftService.sendGift(giftDTO);
        return CommonResp.success();
    }
}
