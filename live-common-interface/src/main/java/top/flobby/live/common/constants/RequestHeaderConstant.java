package top.flobby.live.common.constants;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 常量
 * @create : 2023-12-05 16:29
 **/

public interface RequestHeaderConstant {

    /**
     * 用户登录 ID
     */
    String USER_LOGIN_ID = "live-user-id";

    /**
     * 用户登录 token
     */
    String AUTHORIZATION = "Authorization";

    /**
     * 不需要验证的接口携带的请求头
     */
    String SKIP_VALID = "SKIP_VALID";
}
