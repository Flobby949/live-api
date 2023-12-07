package top.flobby.live.im.interfaces;

/**
 * @author : Flobby
 * @program : live-api
 * @description : token认证rpc
 * @create : 2023-12-07 16:53
 **/

public interface ImTokenRpc {

    /**
     * 创建 IM 登录令牌
     *
     * @param userId 用户 ID
     * @param appId  应用 ID
     * @return {@link String}
     */
    String createImLoginToken(Long userId, Integer appId);

    /**
     * 通过令牌获取用户 ID
     *
     * @param token 令 牌
     * @return {@link Long}
     */
    Long getUserIdByToken(String token);

    /**
     * 通过令牌获取应用 ID
     *
     * @param token 令 牌
     * @return {@link Integer}
     */
    Integer getAppIdByToken(String token);
}
