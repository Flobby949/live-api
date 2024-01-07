package top.flobby.live.api.service;

import top.flobby.live.api.dto.LivingRoomPkReqDTO;
import top.flobby.live.api.dto.StartRedPacketDTO;
import top.flobby.live.common.resp.PageRespVO;
import top.flobby.live.gift.vo.RedPacketReceiveVO;
import top.flobby.live.living.dto.LivingRoomPageDTO;
import top.flobby.live.living.vo.LivingPkRespVO;
import top.flobby.live.living.vo.LivingRoomInfoVO;
import top.flobby.live.living.vo.LivingRoomInitVO;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 直播service
 * @create : 2023-12-12 09:25
 **/

public interface ILivingRoomService {

    /**
     * 直播间列表展示
     *
     * @param livingRoomPageDTO VO
     * @return {@link PageRespVO}<{@link LivingRoomInfoVO}>
     */
    PageRespVO<LivingRoomInfoVO> list(LivingRoomPageDTO livingRoomPageDTO);

    /**
     * 开启直播间
     *
     * @param type 类型
     * @return boolean
     */
    Integer startingLiving(Integer type);

    /**
     * 关闭直播间
     *
     * @param roomId 房间 ID
     * @return boolean
     */
    boolean closeLiving(Integer roomId);

    /**
     * 根据用户id返回当前直播间相关信息
     *
     * @param userId 用户 ID
     * @param roomId 房间 ID
     * @return {@link LivingRoomInitVO}
     */
    LivingRoomInitVO anchorConfig(Long userId, Integer roomId);

    /**
     * 在线PK
     *
     * @param livingRoomPkReqDTO dto
     * @return LivingPkRespVO vo
     */
    LivingPkRespVO onlinePk(LivingRoomPkReqDTO livingRoomPkReqDTO);

    /**
     * PK离线
     *
     * @param livingRoomPkReqDTO DTO
     * @return boolean
     */
    boolean offlinePk(LivingRoomPkReqDTO livingRoomPkReqDTO);

    /**
     * 准备红包
     *
     * @param userId 用户 ID
     * @param roomId 房间 ID
     * @return boolean
     */
    boolean prepareRedPacket(Long userId, Integer roomId);

    /**
     * 开始红包雨
     *
     * @param startRedPacketDTO 启动红包 DTO
     * @return boolean
     */
    boolean startRedPacket(StartRedPacketDTO startRedPacketDTO);


    /**
     * 获取红包
     *
     * @param userId 用户 ID
     * @param code   code
     * @return {@link RedPacketReceiveVO}
     */
    RedPacketReceiveVO getRedPacket(Long userId, String code);
}
