package top.flobby.live.gift.dto;

import lombok.Data;

import java.io.Serial;
import java.util.Date;

/**
 * @author : Flobby
 * @program : live-api
 * @description :
 * @create : 2023-12-17 13:01
 **/

@Data
public class GiftConfigDTO implements java.io.Serializable {

    @Serial
    private static final long serialVersionUID = -9107418926439129985L;

    private Integer giftId;
    private String giftName;
    private Integer price;
    /**
     * 是否有效
     * 0：无效
     * 1：有效
     */
    private Byte status;
    private String coverImgUrl;
    private String svgaUrl;
    private Date createTime;
}
