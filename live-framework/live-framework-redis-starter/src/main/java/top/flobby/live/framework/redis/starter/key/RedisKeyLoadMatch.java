package top.flobby.live.framework.redis.starter.key;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 条件注入类
 * @create : 2023-11-19 13:45
 **/

public class RedisKeyLoadMatch implements Condition {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisKeyLoadMatch.class);
    private static final String PREFIX = "live";

    /**
     * 获取到应用名称，去掉中间的 - 符号，然后前缀加上 live，再和类名进行比较
     * live-user-provider -> liveuserprovider
     * UserProviderCacheKeyBuilder -> liveuserprovidercachekeybuilder
     *
     * @param context  上下文
     * @param metadata 元数据
     * @return boolean
     */
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        String appName =
                context.getEnvironment().getProperty("spring.application.name");
        if (appName == null) {
            LOGGER.error("没有匹配到应用名称，所以无法加载任何 RedisKeyBuilder 对象");
            return false;
        }
        try {
            Field classNameField =
                    metadata.getClass().getDeclaredField("className");
            classNameField.setAccessible(true);
            String keyBuilderName = (String)
                    classNameField.get(metadata);
            List<String> splitList =
                    Arrays.asList(keyBuilderName.split("\\."));
            // 忽略大小写，统一用 live 开头命名
            String classSimplyName = PREFIX +
                    splitList.get(splitList.size() - 1).toLowerCase();
            boolean matchStatus =
                    classSimplyName.contains(appName.replaceAll("-", ""));
            LOGGER.info("keyBuilderClass is {},matchStatus is {}",
                    keyBuilderName, matchStatus);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return true;
    }
}
