package top.flobby.live.gift.provider.service;

import top.flobby.live.gift.dto.GetRedPacketDTO;
import top.flobby.live.gift.provider.dao.po.RedPacketConfigPO;
import top.flobby.live.gift.vo.RedPacketReceiveVO;

/**
 * @author : Flobby
 * @program : live-api
 * @description :
 * @create : 2024-01-07 13:20
 **/

public interface IRedPacketService {

    /**
     * 通过主播ID查询尚未准备红包配置
     *
     * @param anchorId 锚点 ID
     * @return {@link RedPacketConfigPO}
     */
    RedPacketConfigPO queryRedPacketConfigByAnchorId(Long anchorId);

    /**
     * 通过配置代码查询已经准备好的红包
     *
     * @param configCode 配置代码
     * @return {@link RedPacketConfigPO}
     */
    RedPacketConfigPO queryPreparedByConfigCode(String configCode);

    /**
     * 添加红包配置
     *
     * @param redPacketConfigPO 红包配置PO
     * @return boolean
     */
    boolean addRedPacketConfig(RedPacketConfigPO redPacketConfigPO);

    /**
     * 按主播 ID 更新红包
     *
     * @param redPacketConfigPO 红包配置PO
     * @return boolean
     */
    boolean updateRedPacketByAnchorId(RedPacketConfigPO redPacketConfigPO);

    /**
     * 红包生成
     *
     * @param anchorId 主播 ID
     * @return boolean
     */
    boolean prepareRedPacket(Long anchorId);

    /**
     * 红包领取
     *
     * @param getRedPacketDTO DTO
     * @return {@link RedPacketReceiveVO}
     */
    RedPacketReceiveVO receiveRedPacket(GetRedPacketDTO getRedPacketDTO);

    /**
     * 启动红包
     *
     * @param getRedPacketDTO DTO
     * @return boolean
     */
    boolean startRedPacket(GetRedPacketDTO getRedPacketDTO);

    /**
     * 接收红包消息处理
     *
     * @param reqDTO dto
     * @param price  金额
     */
    void receiveRedPacketHandle(GetRedPacketDTO reqDTO, Integer price);
}
