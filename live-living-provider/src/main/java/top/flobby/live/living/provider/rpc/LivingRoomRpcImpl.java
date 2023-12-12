package top.flobby.live.living.provider.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import top.flobby.live.living.dto.LivingRoomReqDTO;
import top.flobby.live.living.interfaces.ILivingRoomRpc;
import top.flobby.live.living.provider.service.ILivingRoomService;
import top.flobby.live.living.vo.LivingRoomInfoVO;

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
    public LivingRoomInfoVO queryByRoomId(Integer roomId) {
        return livingRoomService.queryByRoomId(roomId);
    }

    @Override
    public Integer startLivingRoom(LivingRoomReqDTO livingRoomReqDTO) {
        return livingRoomService.startLivingRoom(livingRoomReqDTO);
    }

    @Override
    public boolean closeLiving(LivingRoomReqDTO livingRoomReqDTO) {
        return livingRoomService.closeLiving(livingRoomReqDTO);
    }
}
