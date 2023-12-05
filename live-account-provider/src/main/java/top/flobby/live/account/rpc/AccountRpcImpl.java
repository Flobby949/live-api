package top.flobby.live.account.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import top.flobby.live.account.interfaces.IAccountRpc;
import top.flobby.live.account.service.IAccountService;

/**
 * @author : Flobby
 * @program : live-api
 * @description :
 * @create : 2023-12-05 13:03
 **/

@DubboService
public class AccountRpcImpl implements IAccountRpc {

    @Resource
    private IAccountService accountService;

    @Override
    public String createAndSaveLoginToken(Long userId) {
        return accountService.createAndSaveLoginToken(userId);
    }

    @Override
    public Long getUserIdByToken(String tokenKey) {
        return accountService.getUserIdByToken(tokenKey);
    }

    @Override
    public void logout(String token) {
        accountService.logout(token);
    }
}
