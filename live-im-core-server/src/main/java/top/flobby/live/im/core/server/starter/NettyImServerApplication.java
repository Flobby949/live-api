package top.flobby.live.im.core.server.starter;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import top.flobby.live.im.core.server.common.ImMsgDecoder;
import top.flobby.live.im.core.server.common.ImMsgEncoder;
import top.flobby.live.im.core.server.handler.ImServerCoreHandler;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 启动类
 * @create : 2023-12-07 14:46
 **/

@RefreshScope
@Configuration
public class NettyImServerApplication implements InitializingBean {

    public static final Logger logger = LoggerFactory.getLogger(NettyImServerApplication.class);

    // 指定一个监听端口
    @Value("${live.im.port}")
    private Integer port;
    @Resource
    private ImServerCoreHandler imServerCoreHandler;

    // 基于netty 启动 java进程，绑定端口
    private void startApplication() throws InterruptedException {
        // 处理 accept 事件
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        // 处理 read 和 write 事件
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup);
        bootstrap.channel(NioServerSocketChannel.class);
        // 初始化相关handler
        bootstrap.childHandler(new ChannelInitializer<>() {
            @Override
            protected void initChannel(Channel channel) throws Exception {
                // 打印日志，方便观察
                logger.info("初始化连接渠道");
                // 设计消息体
                // 增加编码器
                channel.pipeline().addLast(new ImMsgEncoder());
                // 增加解码器
                channel.pipeline().addLast(new ImMsgDecoder());
                // 设置这个netty的handler处理器
                channel.pipeline().addLast(imServerCoreHandler);
            }
        });
        // 基于 JVM 的钩子函数，实现优雅停机
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }));
        ChannelFuture channelFuture = bootstrap.bind(port).sync();
        logger.info("服务端启动成功，端口为：{}", port);
        // 这里会同步阻塞主线程，实现服务长期开启的效果
        channelFuture.channel().closeFuture().sync();
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        Thread nettyServerThread = new Thread(() -> {
            try {
                startApplication();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        nettyServerThread.setName("live-im-server");
        nettyServerThread.start();
    }
}

