package top.flobby.live.im.core.server.bio;

import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author : Flobby
 * @program : io-example
 * @description : Bio 服务端
 * @create : 2023-12-07 11:12
 **/

public class BioServer {
    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket();
        // 绑定 9090 端口
        serverSocket.bind(new InetSocketAddress(9090));
        Socket socket = serverSocket.accept();
        while (true) {
            InputStream inputStream = socket.getInputStream();
            byte[] bytes = new byte[10];
            // 阻塞调用
            inputStream.read(bytes);
            System.out.println("服务端收到的数据：" + new String(bytes));
        }
    }
}
