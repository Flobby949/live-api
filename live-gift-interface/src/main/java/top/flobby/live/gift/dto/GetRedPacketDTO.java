package top.flobby.live.gift.dto;

import lombok.Data;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 抢红包DTO
 * @create : 2024-01-07 19:35
 **/

@Data
public class GetRedPacketDTO {

    private String configCode;

    private Long userId;

    private Integer roomId;
}
