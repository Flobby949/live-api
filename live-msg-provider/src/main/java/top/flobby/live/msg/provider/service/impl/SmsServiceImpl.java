package top.flobby.live.msg.provider.service.impl;

import com.cloopen.rest.sdk.BodyType;
import com.cloopen.rest.sdk.CCPRestSmsSDK;
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
import top.flobby.live.msg.enums.SmsTemplateEnum;
import top.flobby.live.msg.provider.config.CloopenConfig;
import top.flobby.live.msg.provider.config.ThreadPoolManager;
import top.flobby.live.msg.provider.dao.mapper.LiveSmsMapper;
import top.flobby.live.msg.provider.dao.po.SmsPO;
import top.flobby.live.msg.provider.service.ISmsService;

import java.util.Date;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;
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
    @Resource
    private CloopenConfig cloopenConfig;

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
        // 生成验证码，4位，60s有效，同一个手机号60s内只能发送一次
        int code = CommonUtils.generateCode();
        // 保存验证码到redis，60s有效
        redisTemplate.opsForValue().set(codeCacheKey, code, 60, TimeUnit.SECONDS);
        // 发送验证码
        ThreadPoolManager.commonAsyncPool.execute(() -> {
            // 第三方短信服务
            boolean codeStatus = cloopenSendSms(phone, code, SmsTemplateEnum.LOGIN_CODE_TEMPLATE);
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
        if (!CommonUtils.checkPhone(phone) || code == null || code < 1000 || code > 9999) {
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

    /**
     * cloopen 发送短信
     *
     * @param phone 电话
     * @param code  验证码
     * @return boolean
     */
    private boolean cloopenSendSms(String phone, int code, SmsTemplateEnum templateEnum) {
        try {
            log.info(" ============= 创建短信发送通道中 ============= \nphone is {},code is {}", phone, code);
            String serverIp = cloopenConfig.getServerIp();
            // 请求端口
            String serverPort = cloopenConfig.getPort();
            // 主账号,登陆云通讯网站后,可在控制台首页看到开发者主账号ACCOUNT SID和主账号令牌AUTH TOKEN
            String accountSId = cloopenConfig.getAccountSId();
            String accountToken = cloopenConfig.getAccountToken();
            // 请使用管理控制台中已创建应用的APPID
            String appId = cloopenConfig.getAppId();
            CCPRestSmsSDK sdk = new CCPRestSmsSDK();
            sdk.init(serverIp, serverPort);
            sdk.setAccount(accountSId, accountToken);
            sdk.setAppId(appId);
            sdk.setBodyType(BodyType.Type_JSON);
            String to = "18962521753";
            String templateId = templateEnum.getTemplateId();
            String[] datas = {String.valueOf(code), "1"};
            HashMap<String, Object> result = sdk.sendTemplateSMS(to, templateId, datas, "1234", UUID.randomUUID().toString());
            if ("000000".equals(result.get("statusCode"))) {
                // 正常返回输出data包体信息（map）
                HashMap<String, Object> data = (HashMap<String, Object>) result.get("data");
                Set<String> keySet = data.keySet();
                for (String key : keySet) {
                    Object object = data.get(key);
                    log.info(key + " = " + object);
                }
                log.info(" ============= 短信已经发送成功 ============= ");
            } else {
                // 异常返回输出错误码和错误信息
                log.error("错误码=" + result.get("statusCode") + " 错误信息= " + result.get("statusMsg"));
                throw new BusinessException(BusinessExceptionEnum.CODE_SEND_FAIL);
            }
        } catch (Exception e) {
            throw new BusinessException(BusinessExceptionEnum.CODE_SEND_FAIL);
        }
        return true;
    }

}
