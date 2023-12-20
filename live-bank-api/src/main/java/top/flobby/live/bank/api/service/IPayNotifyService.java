package top.flobby.live.bank.api.service;

/**
 * @author : Flobby
 * @program : live-api
 * @description :
 * @create : 2023-12-20 11:08
 **/

public interface IPayNotifyService {

    /**
     * 通知处理
     *
     * @param paramJson 第三方支付回调参数
     * @return {@link String}
     */
    String notifyHandler(String paramJson);
}
