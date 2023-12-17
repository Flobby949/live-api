package top.flobby.live.gift.provider.dao.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 礼物
 * @create : 2023-12-17 12:41
 **/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "t_gift_config")
public class GiftPO {
    @TableId(type = IdType.AUTO)
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
    private Date updateTime;
}
