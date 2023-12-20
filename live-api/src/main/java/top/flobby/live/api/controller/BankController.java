package top.flobby.live.api.controller;

import jakarta.annotation.Resource;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import top.flobby.live.api.dto.ProductReqDTO;
import top.flobby.live.api.service.IBankService;
import top.flobby.live.api.vo.PayProductVO;
import top.flobby.live.api.vo.ProductRespVO;
import top.flobby.live.common.exception.BusinessException;
import top.flobby.live.common.exception.BusinessExceptionEnum;
import top.flobby.live.common.resp.CommonResp;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 支付中台
 * @create : 2023-12-20 08:38
 **/

@RestController
@RequestMapping("/bank")
public class BankController {

    @Resource
    private IBankService bankService;

    @GetMapping("/list/{type}")
    public CommonResp<PayProductVO> payProductList(@PathVariable("type") Integer type) {
        if (type == null) {
            throw new BusinessException(BusinessExceptionEnum.PARAMS_ERROR);
        }
        return CommonResp.success(bankService.payProductList(type));
    }

    @PostMapping("/pay")
    public CommonResp<ProductRespVO> payProduct(@RequestBody ProductReqDTO productReqDTO) {
        if (ObjectUtils.isEmpty(productReqDTO)
                || ObjectUtils.isEmpty(productReqDTO.getProductId())
                || ObjectUtils.isEmpty(productReqDTO.getPaySource())) {
            throw new BusinessException(BusinessExceptionEnum.PARAMS_ERROR);
        }
        return CommonResp.success(bankService.payProduct(productReqDTO));
    }
}
