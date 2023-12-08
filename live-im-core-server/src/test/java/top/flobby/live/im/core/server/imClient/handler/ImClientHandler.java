package top.flobby.live.im.core.server.imClient.handler;

import com.alibaba.fastjson2.JSON;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import top.flobby.live.im.common.AppIdEnum;
import top.flobby.live.im.common.ImMsgCodeEnum;
import top.flobby.live.im.core.server.common.ImMsg;
import top.flobby.live.im.core.server.common.ImMsgDecoder;
import top.flobby.live.im.core.server.common.ImMsgEncoder;
import top.flobby.live.im.dto.ImMsgBody;
import top.flobby.live.im.interfaces.ImTokenRpc;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 客户端测试handler
 * @create : 2023-12-07 18:15
 **/

@Component
public class ImClientHandler implements InitializingBean {

    @DubboReference
    private ImTokenRpc imTokenRpc;

    @Override
    public void afterPropertiesSet() throws Exception {
        Thread thread = new Thread(() -> {
            EventLoopGroup clientGroup = new NioEventLoopGroup();
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(clientGroup);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    System.out.println("初始化连接渠道");
                    socketChannel.pipeline().addLast(new ImMsgDecoder());
                    socketChannel.pipeline().addLast(new ImMsgEncoder());
                    socketChannel.pipeline().addLast(new ClientHandler());
                }
            });
            ChannelFuture channelFuture = null;
            Map<Long, Channel> userChannelMap = new HashMap<>();
            for (int i = 0; i < 10; i++) {
                Long userId = 32111L + i;
                ImMsgBody imMsgBody = ImMsgBody.builder()
                        .appId(AppIdEnum.LIVE_BIZ_ID.getCode())
                        .token(imTokenRpc.createImLoginToken(userId, AppIdEnum.LIVE_BIZ_ID.getCode()))
                        .userId(userId)
                        .build();
                try {
                    channelFuture = bootstrap.connect("localhost", 8888).sync();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                ImMsg loginMsg = ImMsg.build(ImMsgCodeEnum.IM_LOGIN_MSG, JSON.toJSONString(imMsgBody));
                System.out.println("【客户端】发送登录消息：" + loginMsg);
                Channel channel = channelFuture.channel();
                channel.writeAndFlush(loginMsg);
                userChannelMap.put(userId, channel);
            }

            while (true) {
                for (Long userId : userChannelMap.keySet()) {
                    ImMsgBody imMsgBody = ImMsgBody.builder()
                            .appId(AppIdEnum.LIVE_BIZ_ID.getCode())
                            .userId(userId)
                            .build();
                    ImMsg heartBeatMsg = ImMsg.build(ImMsgCodeEnum.IM_HEARTBEAT_MSG, JSON.toJSONString(imMsgBody));
                    System.out.println("【客户端】发送心跳消息：" + heartBeatMsg);
                    userChannelMap.get(userId).writeAndFlush(heartBeatMsg);
                }
                try {
                    Thread.sleep(1000 * 3);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        thread.start();
    }
}
