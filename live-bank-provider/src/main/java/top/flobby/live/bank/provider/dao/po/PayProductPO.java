package top.flobby.live.bank.provider.dao.po;

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
 * @description : 付费产品
 * @create : 2023-12-20 08:06
 **/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_pay_product")
public class PayProductPO {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private Integer price;
    private String extra;
    /**
     * 类型
     * 0 - 直播间产品
     */
    private Byte type;
    private Byte validStatus;
    private Date createTime;
    private Date updateTime;
}
