package top.flobby.live.im.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 消息体DTO
 * @create : 2023-12-07 16:45
 **/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImMsgBody implements java.io.Serializable {

    @Serial
    private static final long serialVersionUID = 260886346780666830L;

    /**
     * 应用 ID
     * 从哪一个服务发起送的消息
     */
    private Integer appId;

    /**
     * 用户 ID
     */
    private Long userId;

    /**
     * 令 牌
     * 从业务中获取，用于在IM服务建立连接时进行身份验证
     */
    private String token;

    /**
     * 消息类型
     */
    private Integer bizCode;

    /**
     * 和业务服务进行消息传递
     */
    private String data;
}
