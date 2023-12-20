package top.flobby.live.bank.api.vo;

import lombok.Data;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 微信回调
 * @create : 2023-12-20 11:11
 **/

@Data
public class WxPayNotifyVO {
    private String orderId;
    private Long userId;
    private Integer bizCode;
}
