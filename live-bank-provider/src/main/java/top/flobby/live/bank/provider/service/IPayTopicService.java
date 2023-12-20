package top.flobby.live.bank.provider.service;

import top.flobby.live.bank.provider.dao.po.PayTopicPO;

/**
 * @author : Flobby
 * @program : live-api
 * @description :
 * @create : 2023-12-20 11:26
 **/

public interface IPayTopicService {

    PayTopicPO queryByBizCode(Integer bizCode);
}
