package top.flobby.live.living.interfaces;

import top.flobby.live.common.resp.PageRespVO;
import top.flobby.live.living.dto.LivingRoomPageDTO;
import top.flobby.live.living.dto.LivingRoomReqDTO;
import top.flobby.live.living.vo.LivingPkRespVO;
import top.flobby.live.living.vo.LivingRoomInfoVO;

import java.util.List;

/**
 * @author : Flobby
 * @program : live-api
 * @description : rpc
 * @create : 2023-12-12 09:54
 **/

public interface ILivingRoomRpc {

    /**
     * 根据用户id查询是否正在开播
     *
     * @param roomId 房间 ID
     * @return {@link LivingRoomInfoVO}
     */
    LivingRoomInfoVO queryLivingRoomByRoomId(Integer roomId);

    /**
     * 开启直播间
     *
     * @param livingRoomReqDTO DTO
     * @return {@link Integer}
     */
    Integer startLivingRoom(LivingRoomReqDTO livingRoomReqDTO);

    /**
     * 关闭直播间
     *
     * @param livingRoomReqDTO DTO
     * @return boolean
     */
    boolean closeLiving(LivingRoomReqDTO livingRoomReqDTO);

    /**
     * 分页查询
     *
     * @param livingRoomPageDTO DTO
     * @return {@link PageRespVO}<{@link LivingRoomInfoVO}>
     */
    PageRespVO<LivingRoomInfoVO> queryByPage(LivingRoomPageDTO livingRoomPageDTO);

    /**
     * 按房间 ID 查询所有userId
     *
     * @param livingRoomReqDTO 客厅要求 DTO
     * @return {@link List}<{@link Long}>
     */
    List<Long> queryUserIdByRoomId(LivingRoomReqDTO livingRoomReqDTO);

    /**
     * 在线PK
     *
     * @param livingRoomReqDTO dto
     * @return LivingPkRespVO vo
     */
    LivingPkRespVO onlinePk(LivingRoomReqDTO livingRoomReqDTO);

    /**
     * PK离线
     *
     * @param livingRoomReqDTO DTO
     * @return boolean
     */
    boolean offlinePk(LivingRoomReqDTO livingRoomReqDTO);

    /**
     * 在线查询PK用户ID
     *
     * @param roomId 房间 ID
     * @return {@link Long}
     */
    Long queryOnlinePkUserId(Integer roomId);
}
