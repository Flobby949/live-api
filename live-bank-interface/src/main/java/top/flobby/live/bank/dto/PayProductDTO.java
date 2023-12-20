package top.flobby.live.bank.dto;

import lombok.Data;

import java.io.Serial;

/**
 * @author : Flobby
 * @program : live-api
 * @description :
 * @create : 2023-12-20 08:16
 **/

@Data
public class PayProductDTO implements java.io.Serializable {
    @Serial
    private static final long serialVersionUID = -5705581217100965186L;

    private Integer id;
    private String name;
    private Integer price;
    private String extra;
    private Byte type;
    private Byte validStatus;
    private String createTime;
    private String updateTime;
}
