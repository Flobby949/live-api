package top.flobby.live.im.interfaces;

/**
 * @author : Flobby
 * @program : live-api
 * @description : rpc
 * @create : 2023-12-09 13:40
 **/

public interface ImOnlineRpc {

    /**
     * 用户是否在线
     *
     * @param userId 用户 ID
     * @param appId  应用 ID
     * @return boolean
     */
    boolean isOnline(Long userId, Integer appId);
}
