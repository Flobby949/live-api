package top.flobby.live.api.vo;

import lombok.Data;

/**
 * @author : Flobby
 * @program : live-api
 * @description : IM 配置信息返回
 * @create : 2023-12-13 17:59
 **/

@Data
public class ImConfigVO {
    private String token;
    private String wsImServerAddress;
    private String tcpImServerAddress;
}
