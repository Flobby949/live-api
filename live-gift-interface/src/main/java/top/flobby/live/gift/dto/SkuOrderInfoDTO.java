package top.flobby.live.gift.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author : Flobby
 * @program : live-api
 * @description :
 * @create : 2024-03-11 12:29
 **/

@Data
public class SkuOrderInfoDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -6256336831958589687L;

    private Long userId;
    private Integer roomId;
    private List<Integer> skuIds;
}
