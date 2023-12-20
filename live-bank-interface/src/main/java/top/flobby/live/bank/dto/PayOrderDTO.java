package top.flobby.live.bank.dto;

import lombok.Data;

import java.io.Serial;
import java.util.Date;

/**
 * @author : Flobby
 * @program : live-api
 * @description :
 * @create : 2023-12-20 10:14
 **/

@Data
public class PayOrderDTO implements java.io.Serializable {

    @Serial
    private static final long serialVersionUID = 1711298427148860162L;
    private String orderId;
    private Integer productId;
    private Byte status;
    private Long userId;
    private Byte payChannel;
    private Integer source;
    private String payTime;
    private Date createTime;
}
