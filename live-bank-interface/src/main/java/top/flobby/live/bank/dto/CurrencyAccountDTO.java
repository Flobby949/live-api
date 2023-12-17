package top.flobby.live.bank.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author : Flobby
 * @program : live-api
 * @description :
 * @create : 2023-12-17 15:21
 **/

@Data
public class CurrencyAccountDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -6118120182612308194L;

    private Long userId;
    private Integer currentBalance;
    private Integer totalCharged;
    /**
     * 状态
     * 0：无效
     * 1：有效
     * 2：冻结
     */
    private Byte status;
}
