package top.flobby.live.framework.redis.starter.key;

import org.springframework.beans.factory.annotation.Value;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 一个统一的 Key 管理类
 * @create : 2023-11-19 13:40
 **/

public class RedisKeyBuilder {
    @Value("${spring.application.name}")
    private String applicationName;
    private static final String SPLIT_ITEM = ":";

    public String getSplitItem() {
        return SPLIT_ITEM;
    }

    public String getPrefix() {
        return applicationName + SPLIT_ITEM;
    }
}
