package top.flobby.live.gift.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 订单准备请求dto
 * @create : 2024-03-11 12:46
 **/

@Data
@AllArgsConstructor
public class PrepareOrderReqDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1623029593487317028L;

    private Long userId;
    private Integer roomId;
}
