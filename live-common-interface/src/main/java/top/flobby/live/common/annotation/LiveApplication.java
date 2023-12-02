package top.flobby.live.common.annotation;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import java.lang.annotation.*;

/**
 * @author :Flobby
 * @date :2023/12/2
 * @description :自定义注解
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SpringBootApplication(scanBasePackages = {"top.flobby.live"})
@EnableDiscoveryClient
public @interface LiveApplication {
}
