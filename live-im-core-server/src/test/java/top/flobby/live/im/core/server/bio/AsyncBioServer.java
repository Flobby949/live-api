package top.flobby.live.im.core.server.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author : Flobby
 * @program : io-example
 * @description : 异步 Bio 服务端
 * @create : 2023-12-07 11:23
 **/

public class AsyncBioServer {
    public static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5,
            10,
            3,
            TimeUnit.MINUTES,
            new ArrayBlockingQueue<>(10)
    );

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress(9090));
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                threadPoolExecutor.execute(() -> {
                    try {
                        InputStream inputStream = socket.getInputStream();
                        byte[] bytes = new byte[10];
                        inputStream.read(bytes);
                        System.out.println("服务端收到的数据：" + new String(bytes));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
