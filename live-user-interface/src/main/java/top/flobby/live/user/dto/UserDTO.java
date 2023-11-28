package top.flobby.live.user.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @author : Flobby
 * @program : live-api
 * @description :
 * @create : 2023-11-18 14:45
 **/

@Data
public class UserDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -8992023107253533352L;

    private Long userId;
    private String nickName;
    private String avatar;
    private String trueName;
    private Integer sex;
    private Date bornDate;
    private Integer workCity;
    private Integer bornCity;
    private Date createTime;
    private Date updateTime;
}
