package top.flobby.live.bank.vo;

import lombok.Data;

import java.io.Serial;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 账户扣减是否成功返回vo
 * @create : 2023-12-17 17:08
 **/

@Data
public class AccountTradeVO implements java.io.Serializable {

    @Serial
    private static final long serialVersionUID = 2587808028572692776L;
    private long userId;
    private boolean operationSuccess;
    private String message;

    public static AccountTradeVO buildFailure(long userId, String message) {
        AccountTradeVO accountTradeVO = new AccountTradeVO();
        accountTradeVO.setUserId(userId);
        accountTradeVO.setOperationSuccess(false);
        accountTradeVO.setMessage(message);
        return accountTradeVO;
    }

    public static AccountTradeVO buildFailure(long userId) {
        return buildFailure(userId, "消费失败");
    }

    public static AccountTradeVO buildSuccess(long userId, String message) {
        AccountTradeVO accountTradeVO = new AccountTradeVO();
        accountTradeVO.setUserId(userId);
        accountTradeVO.setOperationSuccess(true);
        accountTradeVO.setMessage(message);
        return accountTradeVO;
    }

    public static AccountTradeVO buildSuccess(long userId) {
        return buildSuccess(userId, "消费成功");
    }
}
