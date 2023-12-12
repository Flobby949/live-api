package top.flobby.live.living.provider.dao.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 直播间表（t_living_room_record）的实体类。
 *
 * @author Flobby
 */
@Data
@TableName("t_living_room_record")
public class LivingRoomRecordPO {

    /**
     * 主键。
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 主播ID。
     */
    private Long anchorId;

    /**
     * 直播间类型，默认类型0。
     */
    private Byte type;

    /**
     * 直播间状态，0无效，1有效。
     */
    private Byte status;

    /**
     * 直播间名称。
     */
    private String roomName;

    /**
     * 封面图片URL。
     */
    private String covertImg;

    /**
     * 观看人数。
     */
    private Integer watchNum;

    /**
     * 点赞人数。
     */
    private Integer goodNum;

    /**
     * 开播时间。
     */
    private Date startTime;

    /**
     * 关播时间。
     */
    private Date endTime;

    /**
     * 更新时间，自动更新为当前时间戳。
     */
    private Date updateTime;
}
