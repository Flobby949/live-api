package top.flobby.live.user.dto;

import lombok.Data;

/**
 * @author : Flobby
 * @program : live-api
 * @description :
 * @create : 2023-12-03 14:43
 **/

@Data
public class UserPhoneDTO {
    private Long id;
    /**
     * 用户 ID
     */
    private Long userId;
    /**
     * 电话
     */
    private String phone;
    /**
     * 状态
     * 0 - 无效
     * 1 - 有效
     */
    private Byte status;
}
