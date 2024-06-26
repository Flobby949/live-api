package top.flobby.live.living.vo;

import lombok.Data;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 用户当前所在直播VO
 * @create : 2023-12-12 09:24
 **/

@Data
public class LivingRoomInitVO {

    /**
     * 主播 ID
     */
    private Long anchorId;
    private String anchorName;
    /**
     * 封面图
     */
    private String anchorImg;
    /**
     * 房间名称
     */
    private String roomName;
    /**
     * 是否是主播
     */
    private boolean isAnchor;
    /**
     * 房间 ID
     */
    private Integer roomId;
    /**
     * 红包雨代码
     */
    private String redPacketConfigCode;
    /**
     * 进入直播间的用户的信息
     */
    private Long userId;
    private String avatar;
    private String nickName;
    /**
     * 默认壁纸
     */
    private String defaultBgImg;
}
