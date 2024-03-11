package top.flobby.live.gift.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @author : Flobby
 * @program : live-api
 * @description :
 * @create : 2024-03-11 11:54
 **/

@Data
public class SkuOrderInfoVO implements Serializable {

    @Serial
    private static final long serialVersionUID = -6256336831958589687L;

    private Integer id;
    private String skuIdList;
    private Long userId;
    private Integer roomId;
    private Integer status;
    private String extra;
    private Date createTime;
}
