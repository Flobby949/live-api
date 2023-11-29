package top.flobby.live.user.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @author : Flobby
 * @program : live-api
 * @description : dto
 * @create : 2023-11-29 14:23
 **/

@Data
public class UserTagDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -5180401107218041746L;
    
    /**
     * 用户 ID
     */
    private Long userId;
    /**
     * 标签 01
     */
    private Long tagInfo01;
    /**
     * 标签 02
     */
    private Long tagInfo02;
    /**
     * 标签 03
     */
    private Long tagInfo03;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
}

