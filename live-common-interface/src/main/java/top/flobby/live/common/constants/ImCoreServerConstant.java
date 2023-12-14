package top.flobby.live.common.constants;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 常量维护
 * @create : 2023-12-08 13:23
 **/

public interface ImCoreServerConstant {

    /**
     * IM 绑定 IP 的通用 redis key
     */
    String IM_BIND_IP_KEY = "live:im:bind:ip:";

    /**
     * 无应用 ID
     */
    Integer NO_APP_ID = -1;
}
