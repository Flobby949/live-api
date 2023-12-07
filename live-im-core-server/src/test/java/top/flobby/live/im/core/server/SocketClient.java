package top.flobby.live.im.core.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;

/**
 * @author : Flobby
 * @program : io-example
 * @description : socket 客户端
 * @create : 2023-12-07 11:17
 **/

public class SocketClient {
    public static void main(String[] args) throws IOException, InterruptedException {
        // 发送数据
        int count = 0;
        CountDownLatch countDownLatch = new CountDownLatch(1);
        while (count++ < 10) {
            new Thread(() -> {
                try {
                    // 连接 server
                    Socket socket = new Socket();
                    countDownLatch.await();
                    socket.connect(new InetSocketAddress(8888));
                    System.out.println("客户端连接成功");
                    // OutputStream outputStream = socket.getOutputStream();
                    // outputStream.write("client data".getBytes());
                    // outputStream.flush();
                    // System.out.println("客户端发送数据");
                } catch (IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }
        countDownLatch.countDown();
        Thread.sleep(10000);
    }
}
