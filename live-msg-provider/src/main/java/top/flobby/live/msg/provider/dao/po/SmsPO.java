package top.flobby.live.msg.provider.dao.po;

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
 * @description : 短信实体
 * @create : 2023-12-03 13:02
 **/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_sms")
public class SmsPO {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 发送类型
     */
    private Byte sendType;
    /**
     * 验证码
     */
    private Integer code;
    /**
     * 电话
     */
    private String phone;
    /**
     * 发送时间
     */
    private Date sendTime;
    /**
     * 更新时间
     */
    private Date updateTime;
}
