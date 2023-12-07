package top.flobby.live.im.core.server.imClient;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import top.flobby.live.im.common.ImMsgCodeEnum;
import top.flobby.live.im.core.server.common.ImMsg;
import top.flobby.live.im.core.server.common.ImMsgDecoder;
import top.flobby.live.im.core.server.common.ImMsgEncoder;

/**
 * @author : Flobby
 * @program : live-api
 * @description : Im测试客户端
 * @create : 2023-12-07 15:44
 **/

public class ImClientApplication {


    public static void main(String[] args) throws InterruptedException {
        ImClientApplication imClientApplication = new ImClientApplication();
        imClientApplication.startConnection("localhost", 8888);
    }

    private void startConnection(String address, int port) throws InterruptedException {
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
                socketChannel.pipeline().addLast(new ImClientHandler());
            }
        });
        ChannelFuture channelFuture = bootstrap.connect(address, port).sync();
        Channel channel = channelFuture.channel();
        for (int i = 0; i < 20; i++) {
            channel.writeAndFlush(ImMsg.build(ImMsgCodeEnum.IM_LOGIN_MSG, "登录消息"));
            channel.writeAndFlush(ImMsg.build(ImMsgCodeEnum.IM_LOGOUT_MSG, "登出消息"));
            channel.writeAndFlush(ImMsg.build(ImMsgCodeEnum.IM_BUSINESS_MSG, "业务消息"));
            channel.writeAndFlush(ImMsg.build(ImMsgCodeEnum.IM_HEARTBEAT_MSG, "心跳消息"));
            Thread.sleep(3000);
        }
    }
}
