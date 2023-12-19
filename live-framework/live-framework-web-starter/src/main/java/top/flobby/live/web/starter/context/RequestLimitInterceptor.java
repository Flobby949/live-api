package top.flobby.live.web.starter.context;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import top.flobby.live.common.exception.BusinessException;
import top.flobby.live.common.exception.BusinessExceptionEnum;
import top.flobby.live.web.starter.limit.RequestLimit;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 限流拦截器
 * @create : 2023-12-17 13:51
 **/

@Slf4j
public class RequestLimitInterceptor implements HandlerInterceptor {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Value("${spring.application.name}")
    private String applicationName;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 如果是get请求，直接放行
        if ("OPTIONS".equals(request.getMethod()) || "GET".equals(request.getMethod())) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        boolean hasLimit = handlerMethod.getMethod().isAnnotationPresent(RequestLimit.class);
        // 如果没有限流注解，直接放行
        if (!hasLimit) {
            return true;
        }
        Long userId = RequestContext.getUserId();
        // 如果userId为空，直接放行
        if (userId == null) {
            return true;
        }
        // 获取注解
        RequestLimit requestLimit = handlerMethod.getMethod().getAnnotation(RequestLimit.class);
        // (userId + url + requestValue) base64 -> string (key)
        // redis -> key -> set(1) -> increment
        String cacheKey = applicationName + ":" + userId + ":" + request.getRequestURI();
        log.info("接口{}，进行限流缓存", request.getRequestURI());
        int limit = requestLimit.limit();
        int second = requestLimit.second();
        // 从redis中获取当前用户的访问次数
        Integer reqTime = (Integer) Optional.ofNullable(redisTemplate.opsForValue().get(cacheKey)).orElse(0);
        if (reqTime == 0) {
            // 第一次访问，设置过期时间
            redisTemplate.opsForValue().set(cacheKey, 1, second, TimeUnit.SECONDS);
        } else if (reqTime < limit) {
            // 访问次数+1，不需要延长过期时间
            redisTemplate.opsForValue().increment(cacheKey);
        } else {
            // 超过访问次数，直接返回
            log.info("接口{}，访问次数超过限制", request.getRequestURI());
            throw new BusinessException(BusinessExceptionEnum.REQUEST_LIMIT);
        }
        log.info("cacheKey={}，访问次数{}", cacheKey, reqTime);
        return true;
    }
}
