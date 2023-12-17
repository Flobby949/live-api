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
 * @description : 礼物记录表
 * @create : 2023-12-17 12:44
 **/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "t_gift_record")
public class GiftRecordPO {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer giftId;
    private Long userId;
    private Long objectId;
    private Integer price;
    /**
     * 送礼货币类型
     */
    private Byte priceUnit;
    /**
     * 礼物来源，扩展字段
     */
    private Byte source;
    private Date sendTime;
    private Date updateTime;
}
