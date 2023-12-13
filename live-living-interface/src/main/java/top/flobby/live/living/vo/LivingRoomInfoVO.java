package top.flobby.live.living.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 直播间信息VO
 * @create : 2023-12-12 09:28
 **/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LivingRoomInfoVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 3285174955802533024L;

    private Integer id;
    private Byte type;
    private String roomName;
    private Long anchorId;
    private Integer watchNum;
    private Integer goodNum;
    private String covertImg;
}
