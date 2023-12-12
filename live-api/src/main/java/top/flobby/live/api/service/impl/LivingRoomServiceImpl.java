package top.flobby.live.api.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import top.flobby.live.api.service.ILivingRoomService;
import top.flobby.live.common.resp.PageRespVO;
import top.flobby.live.living.dto.LivingRoomPageDTO;
import top.flobby.live.living.dto.LivingRoomReqDTO;
import top.flobby.live.living.interfaces.ILivingRoomRpc;
import top.flobby.live.living.vo.LivingRoomInfoVO;
import top.flobby.live.living.vo.LivingRoomInitVO;
import top.flobby.live.user.dto.UserDTO;
import top.flobby.live.user.interfaces.IUserRpc;
import top.flobby.live.web.starter.RequestContext;

/**
 * @author : Flobby
 * @program : live-api
 * @description : impl
 * @create : 2023-12-12 09:26
 **/

@Slf4j
@Service
public class LivingRoomServiceImpl implements ILivingRoomService {

    @DubboReference
    private ILivingRoomRpc livingRoomRpc;
    @DubboReference
    private IUserRpc userRpc;

    @Override
    public PageRespVO<LivingRoomInfoVO> list(LivingRoomPageDTO livingRoomPageDTO) {
        return null;
    }

    @Override
    public Integer startingLiving(Integer type) {
        UserDTO anchorInfo = userRpc.getByUserId(RequestContext.getUserId());
        LivingRoomReqDTO livingRoomReqDTO = new LivingRoomReqDTO();
        livingRoomReqDTO.setAnchorId(anchorInfo.getUserId());
        livingRoomReqDTO.setRoomName(anchorInfo.getNickName() + "的直播间");
        livingRoomReqDTO.setCovertImg(anchorInfo.getAvatar());
        livingRoomReqDTO.setType(type.byteValue());
        log.info("开启直播，livingRoomReqDTO={}", livingRoomReqDTO);
        return livingRoomRpc.startLivingRoom(livingRoomReqDTO);
    }

    @Override
    public boolean closeLiving(Integer roomId) {
        LivingRoomReqDTO livingRoomReqDTO = new LivingRoomReqDTO();
        livingRoomReqDTO.setId(roomId);
        livingRoomReqDTO.setAnchorId(RequestContext.getUserId());
        return livingRoomRpc.closeLiving(livingRoomReqDTO);
    }

    @Override
    public LivingRoomInitVO anchorConfig(Long userId, Integer roomId) {
        return null;
    }
}
