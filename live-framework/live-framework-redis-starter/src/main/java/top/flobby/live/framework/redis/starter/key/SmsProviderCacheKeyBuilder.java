package top.flobby.live.framework.redis.starter.key;

import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 消息中台的key
 * @create : 2023-11-19 13:41
 **/

@Configuration
@Conditional(RedisKeyLoadMatch.class)
public class SmsProviderCacheKeyBuilder extends RedisKeyBuilder {
    private static final String SMS_LOGIN_CODE_KEY = "smsLoginCode";

    public String buildLoginCodeKey(String phone) {
        return super.getPrefix() + SMS_LOGIN_CODE_KEY +
                super.getSplitItem() + phone;
    }
}
