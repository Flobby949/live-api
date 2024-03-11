package top.flobby.live.gift.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author : Flobby
 * @program : live-api
 * @description : mq库存回滚dto
 * @create : 2024-03-11 13:18
 **/

@Data
public class RollBackStockDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 5579463988767397540L;

    private Long userId;
    private Integer orderId;

}
