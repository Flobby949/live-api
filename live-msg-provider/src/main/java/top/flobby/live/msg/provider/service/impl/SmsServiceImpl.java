package top.flobby.live.msg.provider.service.impl;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import top.flobby.live.common.exception.BusinessException;
import top.flobby.live.common.exception.BusinessExceptionEnum;
import top.flobby.live.common.utils.CommonUtils;
import top.flobby.live.framework.redis.starter.key.SmsProviderCacheKeyBuilder;
import top.flobby.live.msg.dto.MsgCheckDTO;
import top.flobby.live.msg.enums.MsgSendResultEnum;
import top.flobby.live.msg.enums.SmsSendTypeEnum;
import top.flobby.live.msg.provider.config.ThreadPoolManager;
import top.flobby.live.msg.provider.dao.mapper.LiveSmsMapper;
import top.flobby.live.msg.provider.dao.po.SmsPO;
import top.flobby.live.msg.provider.service.ISmsService;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 实现类
 * @create : 2023-12-03 13:03
 **/

@Slf4j
@Service
public class SmsServiceImpl implements ISmsService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private SmsProviderCacheKeyBuilder smsKeyBuilder;
    @Resource
    private LiveSmsMapper smsMapper;

    @Override
    public MsgSendResultEnum sendLoginMsg(String phone) {
        if (!CommonUtils.checkPhone(phone)) {
            return MsgSendResultEnum.MSG_PARAMS_ERROR;
        }
        String codeCacheKey = smsKeyBuilder.buildLoginCodeKey(phone);
        // 判断60s内是否发送过验证码
        if (Boolean.TRUE.equals(redisTemplate.hasKey(codeCacheKey))) {
            log.info("用户 {} 发送验证码过于频繁", phone);
            return MsgSendResultEnum.SEND_TOO_FAST;
        }
        // 生成验证码，6位，60s有效，同一个手机号60s内只能发送一次
        int code = CommonUtils.generateCode();
        // 保存验证码到redis，60s有效
        redisTemplate.opsForValue().set(codeCacheKey, code, 60, TimeUnit.SECONDS);
        // 发送验证码
        ThreadPoolManager.commonAsyncPool.execute(() -> {
            // TODO 第三方短信服务
            boolean codeStatus = mockSendSms(phone, code);
            if (codeStatus) {
                // 保存验证码到数据库
                insertMsg(phone, code, SmsSendTypeEnum.LOGIN_OR_REGISTER);
            }
        });
        return MsgSendResultEnum.SEND_SUCCESS;
    }

    @Override
    public MsgCheckDTO checkLoginMsg(String phone, Integer code) {
        // 参数校验
        if (!CommonUtils.checkPhone(phone) || code == null || code < 100000 || code > 999999) {
            throw new BusinessException(BusinessExceptionEnum.PARAMS_ERROR);
        }
        String codeCacheKey = smsKeyBuilder.buildLoginCodeKey(phone);
        Integer cacheCode = (Integer) redisTemplate.opsForValue().get(codeCacheKey);
        if (cacheCode == null) {
            // 验证码已过期
            return MsgCheckDTO.builder()
                    .checkStatus(false)
                    .desc(MsgSendResultEnum.SMS_TIME_OUT.getDesc())
                    .build();
        }
        if (cacheCode.equals(code)) {
            // 删除验证码,防止重复验证
            redisTemplate.delete(codeCacheKey);
            // 验证码正确
            return MsgCheckDTO.builder()
                    .checkStatus(true)
                    .desc(MsgSendResultEnum.SMS_CHECK_SUCCESS.getDesc())
                    .build();
        }
        return MsgCheckDTO.builder()
                .checkStatus(false)
                .desc(MsgSendResultEnum.SMS_CHECK_FAIL.getDesc())
                .build();
    }

    @Override
    public void insertMsg(String phone, Integer code, SmsSendTypeEnum type) {
        SmsPO sms = SmsPO.builder()
                .phone(phone)
                .code(code)
                .sendType(type.getType())
                .sendTime(new Date())
                .updateTime(new Date())
                .build();
        smsMapper.insert(sms);
    }

    private boolean mockSendSms(String phone, int code) {
        try {
            log.info(" ============= 创建短信发送通道中 ============= \nphone is {},code is {}", phone, code);
            Thread.sleep(1000);
            log.info(" ============= 短信已经发送成功 ============= ");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

}
