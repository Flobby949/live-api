package top.flobby.live.user.provider.dao.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 用户实体
 * @create : 2023-11-18 14:38
 **/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_user")
public class UserPO {

    /**
     * 用户ID。
     * <p>该字段为主键，非空，默认值为 -1，表示用户的唯一标识。</p>
     */
    @TableId(type = IdType.INPUT)
    private Long userId;

    /**
     * 用户昵称。
     * <p>可以为 null，表示用户的昵称。</p>
     */
    private String nickName;

    /**
     * 用户头像的 URL。
     * <p>可以为 null，存储用户头像的链接地址。</p>
     */
    private String avatar;

    /**
     * 用户的真实姓名。
     * <p>可以为 null，表示用户的真实姓名。</p>
     */
    private String trueName;

    /**
     * 用户性别。
     * <p>可以为 null，0 表示男性，1 表示女性。</p>
     */
    private Integer sex;

    /**
     * 用户的出生日期。
     * <p>可以为 null，表示用户的出生日期。</p>
     */
    private Date bornDate;

    /**
     * 工作地城市ID。
     * <p>可以为 null，表示用户的工作地点。</p>
     */
    private Integer workCity;

    /**
     * 出生地城市ID。
     * <p>可以为 null，表示用户的出生地。</p>
     */
    private Integer bornCity;

    /**
     * 记录的创建时间。
     * <p>记录的创建时间，默认由数据库自动生成。</p>
     */
    private Date createTime;

    /**
     * 记录的更新时间。
     * <p>记录的最后更新时间，由数据库在记录更新时自动设置。</p>
     */
    private Date updateTime;

}
