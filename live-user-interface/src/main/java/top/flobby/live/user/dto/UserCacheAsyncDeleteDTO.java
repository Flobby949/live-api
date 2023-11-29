package top.flobby.live.user.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 用户缓存删除DTO
 * @create : 2023-11-29 14:41
 **/

@Data
public class UserCacheAsyncDeleteDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 2105725585645960807L;
    /**
     * 不同的code代表不同的操作，区分不同的删除操作
     */
    private Integer code;
    /**
     * json格式
     */
    private String json;
}
