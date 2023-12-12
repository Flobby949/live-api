package top.flobby.live.living.dto;

import lombok.Data;
import top.flobby.live.common.req.PageBaseReq;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 直播间列表请求
 * @create : 2023-12-12 09:31
 **/

@Data
public class LivingRoomPageDTO extends PageBaseReq {

    private Integer type;
}
