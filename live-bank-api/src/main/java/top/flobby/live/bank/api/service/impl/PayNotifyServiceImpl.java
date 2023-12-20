package top.flobby.live.bank.api.service.impl;

import com.alibaba.fastjson2.JSON;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import top.flobby.live.bank.api.service.IPayNotifyService;
import top.flobby.live.bank.api.vo.WxPayNotifyVO;
import top.flobby.live.bank.constant.PayChannelEnum;
import top.flobby.live.bank.dto.PayOrderDTO;
import top.flobby.live.bank.interfaces.IPayOrderRpc;

/**
 * @author : Flobby
 * @program : live-api
 * @description :
 * @create : 2023-12-20 11:08
 **/

@Service
public class PayNotifyServiceImpl implements IPayNotifyService {

    @DubboReference
    private IPayOrderRpc payOrderRpc;

    @Override
    public String notifyHandler(String paramJson) {
        WxPayNotifyVO wxPayNotifyVO = JSON.parseObject(paramJson, WxPayNotifyVO.class);
        PayOrderDTO payOrderDTO = new PayOrderDTO();
        payOrderDTO.setOrderId(wxPayNotifyVO.getOrderId());
        payOrderDTO.setUserId(wxPayNotifyVO.getUserId());
        payOrderDTO.setBizCode(wxPayNotifyVO.getBizCode());
        payOrderDTO.setPayChannel(PayChannelEnum.WECHAT.getCode());
        boolean notifyResult = payOrderRpc.payNotify(payOrderDTO);
        return notifyResult ? "success" : "fail";
    }
}
