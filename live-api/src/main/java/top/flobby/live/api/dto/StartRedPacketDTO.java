package top.flobby.live.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 开启红包雨dto
 * @create : 2024-01-07 21:32
 **/

@Data
@AllArgsConstructor
public class StartRedPacketDTO {
    private Long anchorId;
    private Long userId;
    private String redPacketConfigCode;
}
