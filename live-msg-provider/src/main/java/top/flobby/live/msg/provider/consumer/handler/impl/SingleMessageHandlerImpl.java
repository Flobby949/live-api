package top.flobby.live.msg.provider.consumer.handler.impl;

import com.alibaba.fastjson2.JSON;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;
import top.flobby.live.im.common.AppIdEnum;
import top.flobby.live.im.dto.ImMsgBody;
import top.flobby.live.im.router.constants.ImMsgBizCodeEnum;
import top.flobby.live.im.router.interfaces.ImRouterRpc;
import top.flobby.live.living.dto.LivingRoomReqDTO;
import top.flobby.live.living.interfaces.ILivingRoomRpc;
import top.flobby.live.msg.dto.MessageDTO;
import top.flobby.live.msg.provider.consumer.handler.MessageHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 实现类
 * @create : 2023-12-08 13:53
 **/

@Component
public class SingleMessageHandlerImpl implements MessageHandler {

    @DubboReference
    private ImRouterRpc imRouterRpc;

    @DubboReference
    private ILivingRoomRpc livingRoomRpc;

    @Override
    public void onMsgReceive(ImMsgBody msgBody) {
        Integer bizCode = msgBody.getBizCode();
        if (ImMsgBizCodeEnum.LIVING_ROOM_IM_CHAT_MSG_BIZ.getCode().equals(bizCode)) {
            // 直播间聊天消息，一个人发送，多个人接收，相当于群聊
            // 根据roomId，appId，获取直播间内的所有人，然后发送消息
            MessageDTO messageDTO = JSON.parseObject(msgBody.getData(), MessageDTO.class);
            Long roomId = messageDTO.getRoomId();
            LivingRoomReqDTO reqDTO = new LivingRoomReqDTO();
            reqDTO.setId(roomId);
            reqDTO.setAppId(msgBody.getAppId());
            // 自己不需要收到自己的消息
            List<Long> userIdList = livingRoomRpc.queryUserIdByRoomId(reqDTO)
                    .stream()
                    .filter(x -> !x.equals(msgBody.getUserId()))
                    .collect(Collectors.toList());
            List<ImMsgBody> imMsgBodyList = new ArrayList<>();
            userIdList.forEach(userId -> {
                ImMsgBody imMsgBody = ImMsgBody.builder()
                        .userId(userId)
                        .appId(AppIdEnum.LIVE_BIZ_ID.getCode())
                        .bizCode(ImMsgBizCodeEnum.LIVING_ROOM_IM_CHAT_MSG_BIZ.getCode())
                        .data(JSON.toJSONString(messageDTO))
                        .build();
                imMsgBodyList.add(imMsgBody);
            });
            // TODO 暂时不做过多处理
            imRouterRpc.batchSendMsg(imMsgBodyList);
        }
    }
}
