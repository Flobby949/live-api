package top.flobby.live.gateway.filter;

import cn.hutool.core.text.CharSequenceUtil;
import com.alibaba.cloud.commons.lang.StringUtils;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import top.flobby.live.account.interfaces.IAccountRpc;
import top.flobby.live.common.utils.JwtUtil;
import top.flobby.live.gateway.properties.GatewayApplicationProperties;

import java.net.URI;
import java.util.List;

import static top.flobby.live.common.constants.RequestHeaderConstant.*;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 登录认证过滤器
 * @create : 2023-12-05 15:02
 **/

@Component
public class AccountCheckFilter implements GlobalFilter, Ordered {

    public static final Logger logger = LoggerFactory.getLogger(AccountCheckFilter.class);

    @DubboReference
    private IAccountRpc accountRpc;
    @Resource
    private GatewayApplicationProperties gatewayApplicationProperties;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        URI uri = request.getURI();
        String urlPath = uri.getPath();
        logger.info("请求路径：{}", urlPath);
        if (StringUtils.isBlank(urlPath)) {
            return Mono.empty();
        }
        if ("true".equals(request.getHeaders().getFirst(SKIP_VALID))) {
            logger.info("跳过认证,{}", request.getURI().getPath());
            return chain.filter(exchange);
        }
        List<String> filterUrlList = gatewayApplicationProperties.getNotCheckUrlList();
        for (String urlItem : filterUrlList) {
            if (urlPath.startsWith(urlItem)) {
                logger.info("不需要认证的url:{}", urlPath);
                return chain.filter(exchange);
            }
        }
        // 请求头获取token
        String token = request.getHeaders().getFirst(AUTHORIZATION);
        ServerHttpResponse response = exchange.getResponse();
        logger.info("会员登录验证开始，token：{}", token);
        if (CharSequenceUtil.isBlank(token)) {
            logger.warn("token 为空，请求被拦截！, {}", urlPath);
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }
        // 校验 token 是否有效
        boolean validate = JwtUtil.validate(token);
        if (!validate) {
            logger.warn("token 无效，拦截");
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }
        try {
            Long userId = accountRpc.getUserIdByToken(token);
            logger.info("网关获取userId:{}", userId);
            ServerHttpRequest.Builder builder = request.mutate();
            // 把 userId 传递给到下游去
            builder.header(USER_LOGIN_ID, String.valueOf(userId));
            return chain.filter(exchange.mutate().request(builder.build()).build());
        } catch (Exception e) {
            logger.error("token 无效，拦截");
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
