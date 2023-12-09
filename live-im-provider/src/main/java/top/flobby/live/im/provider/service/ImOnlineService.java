package top.flobby.live.im.provider.service;

/**
 * @author : Flobby
 * @program : live-api
 * @description : service
 * @create : 2023-12-09 13:41
 **/

public interface ImOnlineService {

    /**
     * 用户是否在线
     *
     * @param userId 用户 ID
     * @param appId  应用 ID
     * @return boolean
     */
    boolean isOnline(Long userId, Integer appId);
}
