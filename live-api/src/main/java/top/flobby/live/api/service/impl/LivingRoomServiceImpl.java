package top.flobby.live.api.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import top.flobby.live.api.dto.LivingRoomPkReqDTO;
import top.flobby.live.api.dto.StartRedPacketDTO;
import top.flobby.live.api.service.ILivingRoomService;
import top.flobby.live.common.exception.BusinessException;
import top.flobby.live.common.exception.BusinessExceptionEnum;
import top.flobby.live.common.resp.PageRespVO;
import top.flobby.live.gift.dto.GetRedPacketDTO;
import top.flobby.live.gift.dto.RedPacketConfigDTO;
import top.flobby.live.gift.interfaces.IRedPacketRpc;
import top.flobby.live.gift.vo.RedPacketReceiveVO;
import top.flobby.live.im.common.AppIdEnum;
import top.flobby.live.living.dto.LivingRoomPageDTO;
import top.flobby.live.living.dto.LivingRoomReqDTO;
import top.flobby.live.living.interfaces.ILivingRoomRpc;
import top.flobby.live.living.vo.LivingPkRespVO;
import top.flobby.live.living.vo.LivingRoomInfoVO;
import top.flobby.live.living.vo.LivingRoomInitVO;
import top.flobby.live.user.dto.UserDTO;
import top.flobby.live.user.interfaces.IUserRpc;
import top.flobby.live.web.starter.context.RequestContext;

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
    @DubboReference
    private IRedPacketRpc redPacketRpc;

    @Override
    public PageRespVO<LivingRoomInfoVO> list(LivingRoomPageDTO livingRoomPageDTO) {
        return livingRoomRpc.queryByPage(livingRoomPageDTO);
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
        LivingRoomInfoVO info = livingRoomRpc.queryLivingRoomByRoomId(roomId);
        Long anchorId = info.getAnchorId();
        LivingRoomInitVO respVo = new LivingRoomInitVO();
        if (ObjectUtils.isEmpty(info) || anchorId == null) {
            // 直播间不存在
            throw new BusinessException(BusinessExceptionEnum.LIVING_ROOM_IS_NOT_EXIST);
        }
        respVo.setUserId(0L);
        if (userId != null) {
            UserDTO userDTO = userRpc.getByUserId(userId);
            respVo.setUserId(userDTO.getUserId());
            respVo.setAvatar(userDTO.getAvatar());
            respVo.setNickName(userDTO.getNickName());
        }
        UserDTO anchor = userRpc.getByUserId(anchorId);
        respVo.setAnchorId(anchorId);
        respVo.setAnchorImg(anchor.getAvatar());
        respVo.setAnchorName(anchor.getNickName());
        boolean isAnchor = anchorId.equals(userId);
        respVo.setAnchor(isAnchor);
        respVo.setRoomId(info.getId());
        if (isAnchor) {
            RedPacketConfigDTO redPacketConfig = redPacketRpc.queryRedPacketConfigByAnchorId(anchorId);
            if (redPacketConfig != null) {
                // 如果主播有红包雨配置，返回红包雨配置代码
                respVo.setRedPacketConfigCode(redPacketConfig.getConfigCode());
            }
        }
        return respVo;
    }

    @Override
    public LivingPkRespVO onlinePk(LivingRoomPkReqDTO livingRoomPkReqDTO) {
        LivingRoomReqDTO livingRoomReqDTO = new LivingRoomReqDTO();
        livingRoomReqDTO.setAppId(AppIdEnum.LIVE_BIZ_ID.getCode());
        livingRoomReqDTO.setId(livingRoomPkReqDTO.getRoomId());
        livingRoomReqDTO.setPkObjId(RequestContext.getUserId());
        return livingRoomRpc.onlinePk(livingRoomReqDTO);
    }

    @Override
    public boolean offlinePk(LivingRoomPkReqDTO livingRoomPkReqDTO) {
        LivingRoomReqDTO livingRoomReqDTO = new LivingRoomReqDTO();
        livingRoomReqDTO.setId(livingRoomPkReqDTO.getRoomId());
        livingRoomReqDTO.setAnchorId(RequestContext.getUserId());
        return livingRoomRpc.offlinePk(livingRoomReqDTO);
    }

    @Override
    public boolean prepareRedPacket(Long userId, Integer roomId) {
        LivingRoomInfoVO livingRoom = livingRoomRpc.queryLivingRoomByRoomId(roomId);
        if (livingRoom == null) {
            throw new BusinessException(BusinessExceptionEnum.LIVING_ROOM_IS_NOT_EXIST);
        }
        if (!livingRoom.getAnchorId().equals(userId)) {
            throw new BusinessException(BusinessExceptionEnum.USER_IS_NOT_ANCHOR);
        }
        return redPacketRpc.prepareRedPacket(userId);
    }

    @Override
    public boolean startRedPacket(StartRedPacketDTO startRedPacketDTO) {
        GetRedPacketDTO getRedPacketDTO = new GetRedPacketDTO();
        getRedPacketDTO.setUserId(startRedPacketDTO.getUserId());
        getRedPacketDTO.setConfigCode(startRedPacketDTO.getRedPacketConfigCode());
        LivingRoomInfoVO livingRoomInfo = livingRoomRpc.queryLivingRoomByAnchorId(startRedPacketDTO.getAnchorId());
        if (ObjectUtils.isEmpty(livingRoomInfo) || livingRoomInfo.getId() == null) {
            throw new BusinessException(BusinessExceptionEnum.LIVING_ROOM_IS_NOT_EXIST);
        }
        getRedPacketDTO.setRoomId(livingRoomInfo.getId());
        return redPacketRpc.startRedPacket(getRedPacketDTO);
    }

    @Override
    public RedPacketReceiveVO getRedPacket(Long userId, String code) {
        GetRedPacketDTO getRedPacketDTO = new GetRedPacketDTO();
        getRedPacketDTO.setUserId(userId);
        getRedPacketDTO.setConfigCode(code);
        RedPacketReceiveVO resultVO = redPacketRpc.receiveRedPacket(getRedPacketDTO);
        if (resultVO == null) {
            throw new BusinessException(BusinessExceptionEnum.RED_PACKET_IS_NOT_ENOUGH);
        }
        return resultVO;
    }
}
