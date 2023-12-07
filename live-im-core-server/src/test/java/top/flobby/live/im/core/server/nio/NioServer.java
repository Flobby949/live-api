package top.flobby.live.im.core.server.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : Flobby
 * @program : io-example
 * @description : Nio server
 * @create : 2023-12-07 11:30
 **/

public class NioServer {
    private static List<SocketChannel> socketChannelList = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        ServerSocketChannel channel = ServerSocketChannel.open();
        channel.socket().bind(new InetSocketAddress(9090));
        channel.configureBlocking(false);
        new Thread(() -> {
            while (true) {
                // 假设 这里有大量的socket对象，CPU一直在轮询，浪费资源
                for (SocketChannel socketChannel : socketChannelList) {
                    try {
                        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
                        // 非阻塞调用，没有数据也会继续执行
                        socketChannel.read(byteBuffer);
                        System.out.println("服务端收到的数据：" + new String(byteBuffer.array()));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
        while (true) {
            SocketChannel socketChannel = channel.accept();
            if (socketChannel != null) {
                System.out.println("连接成功");
                socketChannel.configureBlocking(false);
                socketChannelList.add(socketChannel);
            }
        }
    }
}
