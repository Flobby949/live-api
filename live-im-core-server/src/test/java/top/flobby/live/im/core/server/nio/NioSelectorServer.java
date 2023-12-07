package top.flobby.live.im.core.server.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author : Flobby
 * @program : io-example
 * @description : selector的Nio服务端
 * @create : 2023-12-07 11:41
 **/

public class NioSelectorServer {
    /*标识数字*/
    private int flag = 0;
    /*缓冲区大小*/
    private int BLOCK = 4096;
    /*接受数据缓冲区*/
    private ByteBuffer sendBuffer = ByteBuffer.allocate(BLOCK);
    /*发送数据缓冲区*/
    private ByteBuffer receiveBuffer = ByteBuffer.allocate(BLOCK);
    private Selector selector;

    public NioSelectorServer(int port) throws IOException {
        // 打开服务器套接字通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // 服务器配置为非阻塞
        serverSocketChannel.configureBlocking(false);
        // 检索与此通道关联的服务器套接字
        ServerSocket socket = serverSocketChannel.socket();
        // 进行服务的绑定
        socket.bind(new InetSocketAddress(port));
        // 通过open()方法找到Selector，高性能组件
        // selector 的底层实现是根据不同系统的JDK决定的，不同环境实现不同
        selector = Selector.open();
        System.out.println(selector);
        // 注册到selector，等待连接
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("服务已启动，监听端口是：" + port);
    }

    private void listen() throws IOException {
        while (true) {
            // 如果外界没有任何读写请求，这里会阻塞，让出当前CPU
            selector.select();
            System.out.println("开始处理请求...");
            // 返回此选择器的已选择键集
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();
                handleKey(key);
            }
        }
    }

    private void handleKey(SelectionKey selectionKey) throws IOException {
        // 接受请求
        ServerSocketChannel server = null;
        SocketChannel client = null;
        String receiveText;
        String sendText;
        int count = 0;
        // 测试此键的通道是否已准备好接受新的套接字连接
        if (selectionKey.isAcceptable()) {
            // 返回为之创建此键的通道
            server = (ServerSocketChannel) selectionKey.channel();
            // 接受到此通道套接字的连接，非阻塞模式下，不会阻塞
            client = server.accept();
            // 配置为非阻塞
            client.configureBlocking(false);
            // 注册到selector，等待连接
            client.register(selector, SelectionKey.OP_READ);
        } else if (selectionKey.isReadable()) {
            // 返回为之创建此键的通道
            client = (SocketChannel) selectionKey.channel();
            // 缓冲区清空，以备下一次读取
            receiveBuffer.clear();
            // 将读取到的数据，放入缓冲区
            count = client.read(receiveBuffer);
            if (count > 0) {
                receiveText = new String(receiveBuffer.array(), 0, count);
                System.out.println("服务端接收数据：" + receiveText);
                client.register(selector, SelectionKey.OP_WRITE);
            } else if (selectionKey.isWritable()) {
                // 清空写入缓冲区
                sendBuffer.clear();
                // 返回为之创建此键的通道
                client = (SocketChannel) selectionKey.channel();
                sendText = "message from server--" + flag++;
                // 向缓冲区中输入数据
                sendBuffer.put(sendText.getBytes());
                // 将缓冲区各标志复位，因为向里面 put 了数据，标志被改变。
                // 想要从缓冲区中读取数据发送到服务器，就要复位
                sendBuffer.flip();
                // 输出到通道
                client.write(sendBuffer);
                System.out.println("服务器向客户端发送数据--：" + sendText);
                client.register(selector, SelectionKey.OP_READ);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        int port = 9090;
        NioSelectorServer server = new NioSelectorServer(port);
        server.listen();
    }
}
