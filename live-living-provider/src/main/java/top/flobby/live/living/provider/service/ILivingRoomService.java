package top.flobby.live.living.provider.service;

import top.flobby.live.common.resp.PageRespVO;
import top.flobby.live.living.dto.LivingRoomPageDTO;
import top.flobby.live.living.dto.LivingRoomReqDTO;
import top.flobby.live.living.vo.LivingRoomInfoVO;

import java.util.List;

/**
 * @author : Flobby
 * @program : live-api
 * @description : service
 * @create : 2023-12-12 09:54
 **/

public interface ILivingRoomService {

    /**
     * 根据用户id查询是否正在开播
     *
     * @param roomId 房间 ID
     * @return {@link LivingRoomInfoVO}
     */
    LivingRoomInfoVO queryByRoomId(Integer roomId);

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
     * 从数据库中查询直播间信息
     *
     * @param type 类型
     * @return {@link List}<{@link LivingRoomInfoVO}>
     */
    List<LivingRoomInfoVO> queryAllListFromDb(Integer type);
}
