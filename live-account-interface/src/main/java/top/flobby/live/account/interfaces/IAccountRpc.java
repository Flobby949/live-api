package top.flobby.live.account.interfaces;

/**
 * @author : Flobby
 * @program : live-api
 * @description : rpc
 * @create : 2023-12-05 13:00
 **/

public interface IAccountRpc {

    /**
     * 创建并保存登录令牌
     *
     * @param userId 用户 ID
     * @return {@link String}
     */
    String createAndSaveLoginToken(Long userId);

    /**
     * 校验用户 token
     *
     * @param tokenKey 令牌密钥
     * @return {@link Long}
     */
    Long getUserIdByToken(String tokenKey);

    /**
     * 注销
     *
     * @param token 令 牌
     */
    void logout(String token);

}
