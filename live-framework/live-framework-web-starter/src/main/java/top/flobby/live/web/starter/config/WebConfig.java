package top.flobby.live.web.starter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.flobby.live.web.starter.context.RequestLimitInterceptor;
import top.flobby.live.web.starter.context.UserInfoInterceptor;

/**
 * @author : Flobby
 * @program : live-api
 * @description :
 * @create : 2023-12-05 15:58
 **/

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public UserInfoInterceptor userInfoInterceptor() {
        return new UserInfoInterceptor();
    }

    @Bean
    public RequestLimitInterceptor requestLimitInterceptor() {
        return new RequestLimitInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userInfoInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/error");
        registry.addInterceptor(requestLimitInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/error");
    }
}
