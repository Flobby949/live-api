package top.flobby.live.msg.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : Flobby
 * @program : live-api
 * @description :
 * @create : 2023-12-03 13:04
 **/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MsgCheckDTO {
    /**
     * 是否成功
     */
    private Boolean checkStatus;
    /**
     * 描述
     */
    private String desc;
}
