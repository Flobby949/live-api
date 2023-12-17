package top.flobby.live.web.starter.limit;

import java.lang.annotation.*;

/**
 * @author :Flobby
 * @date :2023/12/17
 * @description :限流注解
 */

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestLimit {

    /**
     * 限制请求次数
     *
     * @return int
     */
    int limit();

    /**
     * 限流时长
     *
     * @return int
     */
    int second();

    /**
     * 限流提示信息
     *
     * @return {@link String}
     */
    String message() default "请求过于频繁，请稍后再试";
}
