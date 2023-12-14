package top.flobby.live.im.core.server.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 下线dto
 * @create : 2023-12-14 14:31
 **/

@Data
public class ImOfflineDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -8500140043363863191L;
    private Long userId;
    private Integer appId;
    private Long roomId;
    private Long logoutTime;
}
