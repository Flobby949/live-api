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
 * @description : 虚拟货币账户
 * @create : 2023-12-17 15:16
 **/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_currency_account")
public class CurrencyAccountPO {

    @TableId(type = IdType.INPUT)
    private Long userId;
    private Integer currentBalance;
    private Integer totalCharged;
    /**
     * 状态
     * 0：无效
     * 1：有效
     * 2：冻结
     */
    private Byte status;
    private Date createTime;
    private Date updateTime;
}
