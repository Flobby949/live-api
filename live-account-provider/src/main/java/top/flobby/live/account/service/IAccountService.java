package top.flobby.live.account.service;

/**
 * @author : Flobby
 * @program : live-api
 * @description : service
 * @create : 2023-12-05 13:04
 **/

public interface IAccountService {
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
}
