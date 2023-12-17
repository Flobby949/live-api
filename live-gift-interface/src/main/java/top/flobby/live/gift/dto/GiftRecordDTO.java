package top.flobby.live.gift.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @author : Flobby
 * @program : live-api
 * @description :
 * @create : 2023-12-17 13:16
 **/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GiftRecordDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -8344849802692163811L;

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
}
