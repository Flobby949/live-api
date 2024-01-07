package top.flobby.live.gift.interfaces;

import top.flobby.live.gift.dto.RedPacketConfigDTO;
import top.flobby.live.gift.dto.RedPacketReqDTO;

/**
 * @author : Flobby
 * @program : live-api
 * @description :
 * @create : 2024-01-07 13:21
 **/

public interface IRedPacketRpc {

    /**
     * 通过主播ID查询红包配置
     *
     * @param anchorId 锚点 ID
     * @return {@link RedPacketConfigDTO}
     */
    RedPacketConfigDTO queryRedPacketConfigByAnchorId(Long anchorId);

    /**
     * 添加红包配置
     *
     * @param reqDTO 红包配置PO
     * @return boolean
     */
    boolean addRedPacketConfig(RedPacketReqDTO reqDTO);

    /**
     * 按主播 ID 更新红包
     *
     * @param reqDTO 红包配置PO
     * @return boolean
     */
    boolean updateRedPacketByAnchorId(RedPacketReqDTO reqDTO);
}
