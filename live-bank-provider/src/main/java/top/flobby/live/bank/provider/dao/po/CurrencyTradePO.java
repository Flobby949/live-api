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
 * @description : 流水记录表
 * @create : 2023-12-18 13:18
 **/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_currency_trade")
public class CurrencyTradePO {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Integer num;
    private Byte type;
    private Byte status;
    private Date createTime;
    private Date updateTime;
}
