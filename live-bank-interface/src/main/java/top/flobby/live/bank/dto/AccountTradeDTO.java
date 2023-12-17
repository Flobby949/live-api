package top.flobby.live.bank.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 用户交易请求dto
 * @create : 2023-12-17 17:10
 **/

@Data
@Builder
public class AccountTradeDTO implements java.io.Serializable {

    @Serial
    private static final long serialVersionUID = 5469483303920036063L;

    private long userId;
    /**
     * 金额
     */
    private int tradeAmount;
}
