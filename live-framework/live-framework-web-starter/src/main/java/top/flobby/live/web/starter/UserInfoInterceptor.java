package top.flobby.live.web.starter;

import com.alibaba.cloud.commons.lang.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import static top.flobby.live.common.constants.RequestHeaderConstant.USER_LOGIN_ID;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 用户信息拦截器
 * @create : 2023-12-05 15:31
 **/

public class UserInfoInterceptor implements HandlerInterceptor {

    // 所有web请求来到这里的时候，都要被拦截
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userIdStr = request.getHeader(USER_LOGIN_ID);
        // 参数判断，userID是否为空
        // 可能走的是白名单url
        if (StringUtils.isEmpty(userIdStr)) {
            return true;
        }
        // 如果userId不为空，则把它放在线程本地变量里面去
        RequestContext.put(RequestConstants.LIVE_USER_ID, Long.valueOf(userIdStr));
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        RequestContext.clear();
    }
}
