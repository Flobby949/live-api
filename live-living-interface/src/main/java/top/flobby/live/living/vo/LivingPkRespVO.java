package top.flobby.live.living.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author : Flobby
 * @program : live-api
 * @description :
 * @create : 2023-12-26 08:19
 **/

@Data
public class LivingPkRespVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 6226169079531751577L;

    private boolean onlineStatus;

    private String msg;
}
