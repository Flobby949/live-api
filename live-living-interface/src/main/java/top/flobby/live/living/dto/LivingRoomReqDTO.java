package top.flobby.live.living.dto;

import lombok.Data;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 直播间相关请求DTO
 * @create : 2023-12-12 10:05
 **/

@Data
public class LivingRoomReqDTO {

    private Integer id;
    private Long anchorId;
    private String roomName;
    private String covertImg;
    private Byte type;
}
