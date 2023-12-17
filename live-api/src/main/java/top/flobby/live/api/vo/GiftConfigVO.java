package top.flobby.live.api.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 礼物列表返回vo
 * @create : 2023-12-17 16:55
 **/


@Data
public class GiftConfigVO {
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
