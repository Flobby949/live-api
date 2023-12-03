package top.flobby.live.user.req;

import lombok.Data;

/**
 * @author : Flobby
 * @program : live-api
 * @description :
 * @create : 2023-12-03 18:32
 **/

@Data
public class UserLoginReq {
    private String phone;
    private Integer code;
}
