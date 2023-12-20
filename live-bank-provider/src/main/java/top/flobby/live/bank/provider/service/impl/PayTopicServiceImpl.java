package top.flobby.live.bank.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import top.flobby.live.bank.provider.dao.mapper.PayTopicMapper;
import top.flobby.live.bank.provider.dao.po.PayTopicPO;
import top.flobby.live.bank.provider.service.IPayTopicService;
import top.flobby.live.common.enums.CommonStatusEnum;

/**
 * @author : Flobby
 * @program : live-api
 * @description :
 * @create : 2023-12-20 11:26
 **/

@Service
public class PayTopicServiceImpl implements IPayTopicService {

    @Resource
    private PayTopicMapper payTopicMapper;

    @Override
    public PayTopicPO queryByBizCode(Integer bizCode) {
        LambdaQueryWrapper<PayTopicPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PayTopicPO::getBizCode, bizCode);
        wrapper.eq(PayTopicPO::getStatus, CommonStatusEnum.VALID.getCode());
        return payTopicMapper.selectOne(wrapper);
    }
}
