package top.flobby.live.gift.provider.service.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.flobby.live.gift.dto.GetRedPacketDTO;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 红包消息
 * @create : 2024-01-12 14:39
 **/

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SendRedPacketBO {

    private GetRedPacketDTO reqDTO;

    private Integer price;
}
