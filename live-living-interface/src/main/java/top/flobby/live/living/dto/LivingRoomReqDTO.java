package top.flobby.live.living.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 直播间相关请求DTO
 * @create : 2023-12-12 10:05
 **/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LivingRoomReqDTO {

    private Integer id;
    private Long anchorId;
    private Long pkObjId;
    private String roomName;
    private String covertImg;
    private Byte type;
    private Integer appId;
}
