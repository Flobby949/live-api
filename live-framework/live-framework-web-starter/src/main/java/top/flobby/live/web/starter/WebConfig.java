package top.flobby.live.web.starter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author : Flobby
 * @program : live-api
 * @description :
 * @create : 2023-12-05 15:58
 **/

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public UserInfoInterceptor qiyuUserInfoInterceptor() {
        return new UserInfoInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(qiyuUserInfoInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/error");
    }
}
