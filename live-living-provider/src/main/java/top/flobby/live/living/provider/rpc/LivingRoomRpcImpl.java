package top.flobby.live.living.provider.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import top.flobby.live.common.resp.PageRespVO;
import top.flobby.live.living.dto.LivingRoomPageDTO;
import top.flobby.live.living.dto.LivingRoomReqDTO;
import top.flobby.live.living.interfaces.ILivingRoomRpc;
import top.flobby.live.living.provider.service.ILivingRoomService;
import top.flobby.live.living.vo.LivingPkRespVO;
import top.flobby.live.living.vo.LivingRoomInfoVO;

import java.util.List;

/**
 * @author : Flobby
 * @program : live-api
 * @description :
 * @create : 2023-12-12 09:55
 **/

@DubboService
public class LivingRoomRpcImpl implements ILivingRoomRpc {

    @Resource
    private ILivingRoomService livingRoomService;


    @Override
    public LivingRoomInfoVO queryLivingRoomByRoomId(Integer roomId) {
        return livingRoomService.queryLivingRoomByRoomId(roomId);
    }

    @Override
    public Integer startLivingRoom(LivingRoomReqDTO livingRoomReqDTO) {
        return livingRoomService.startLivingRoom(livingRoomReqDTO);
    }

    @Override
    public boolean closeLiving(LivingRoomReqDTO livingRoomReqDTO) {
        return livingRoomService.closeLiving(livingRoomReqDTO);
    }

    @Override
    public PageRespVO<LivingRoomInfoVO> queryByPage(LivingRoomPageDTO livingRoomPageDTO) {
        return livingRoomService.queryByPage(livingRoomPageDTO);
    }

    @Override
    public List<Long> queryUserIdByRoomId(LivingRoomReqDTO livingRoomReqDTO) {
        return livingRoomService.queryUserIdsByRoomId(livingRoomReqDTO);
    }

    @Override
    public LivingPkRespVO onlinePk(LivingRoomReqDTO livingRoomReqDTO) {
        return livingRoomService.onlinePk(livingRoomReqDTO);
    }

    @Override
    public boolean offlinePk(LivingRoomReqDTO livingRoomReqDTO) {
        return livingRoomService.offlinePk(livingRoomReqDTO);
    }

    @Override
    public Long queryOnlinePkUserId(Integer roomId) {
        return livingRoomService.queryOnlinePkUserId(roomId);
    }
}
