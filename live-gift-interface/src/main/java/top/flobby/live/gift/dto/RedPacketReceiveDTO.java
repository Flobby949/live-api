package top.flobby.live.gift.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 收到红包的返回DTO
 * @create : 2024-01-07 14:37
 **/

@Data
@Builder
public class RedPacketReceiveDTO implements java.io.Serializable {

    @Serial
    private static final long serialVersionUID = -8386349662514640538L;

    /**
     * 金额
     */
    private Integer price;
}
