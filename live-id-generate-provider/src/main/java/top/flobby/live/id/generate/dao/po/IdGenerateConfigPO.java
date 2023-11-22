package top.flobby.live.id.generate.dao.po;

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
 * @description : PO实体
 * @create : 2023-11-20 11:27
 **/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_id_generate_config")
public class IdGenerateConfigPO {
    /**
     * 主键 ID。
     */
    @TableId
    private Integer id;

    /**
     * 描述。
     */
    private String remark;

    /**
     * 当前 ID 所在阶段的阈值。
     */
    private Long nextThreshold;

    /**
     * 初始化值。
     */
    private Long initNum;

    /**
     * 当前 ID 所在阶段的开始值。
     */
    private Long currentStart;

    /**
     * ID 递增区间。
     */
    private Integer step;

    /**
     * 是否有序（0 无序，1 有序）。
     * false 无序，true 有序。
     */
    private Boolean isSeq;

    /**
     * 业务前缀码，如果没有则返回时不携带。
     */
    private String idPrefix;

    /**
     * 乐观锁版本号。
     */
    private Integer version;

    /**
     * 创建时间戳。
     */
    private Date createTime;

    /**
     * 更新时间戳。
     */
    private Date updateTime;
}
