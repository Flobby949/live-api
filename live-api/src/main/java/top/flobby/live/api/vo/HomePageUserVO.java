package top.flobby.live.api.vo;

import lombok.Data;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 首页用户信息返回vo
 * @create : 2023-12-10 14:34
 **/

@Data
public class HomePageUserVO {

    private Long userId;
    private String nickName;
    private String avatar;
    private Integer sex;
    /**
     * 是否有开播权限
     */
    private Boolean isVip;
}
