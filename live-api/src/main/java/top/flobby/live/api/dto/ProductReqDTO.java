package top.flobby.live.api.dto;

import lombok.Data;
import top.flobby.live.bank.constant.PaySourceEnum;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 支付请求DTO
 * @create : 2023-12-20 09:16
 **/

@Data
public class ProductReqDTO {
    /**
     * 产品 ID
     */
    private Integer productId;
    /**
     * 支付来源
     * 1. 直播间内支付
     * 2. 个人中心支付
     * 3. 聊天页面支付
     * 4. 第三方宣传页面支付
     * 5. 广告弹窗支付
     * 6. 等等
     * 根据不同的来源，统计转换率
     *
     * @see PaySourceEnum
     */
    private Integer paySource;

    /**
     * 支付渠道
     *
     * @see top.flobby.live.bank.constant.PayChannelEnum
     */
    private Byte payChannel;
}
