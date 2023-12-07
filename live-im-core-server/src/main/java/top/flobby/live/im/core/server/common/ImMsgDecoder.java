package top.flobby.live.im.core.server.common;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import top.flobby.live.im.common.ImConstant;

import java.util.List;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 解码器
 * @create : 2023-12-07 15:14
 **/

public class ImMsgDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) throws Exception {
        // byteBuf 的基本校验
        if (byteBuf.readableBytes() < ImConstant.BASE_LENGTH) {
            ctx.close();
            return;
        }
        // 魔数magic校验
        if (byteBuf.readShort() != ImConstant.DEFAULT_MAGIC) {
            ctx.close();
            return;
        }
        // code和len的顺序，取决于消息编码时的顺序
        int code = byteBuf.readInt();
        int len = byteBuf.readInt();
        // 长度校验，确保消息体的完整性
        if (byteBuf.readableBytes() < len) {
            ctx.close();
            return;
        }
        byte[] body = new byte[len];
        byteBuf.readBytes(body);
        // byteBuf 转化为 imMsg 对象
        ImMsg imMsg = new ImMsg();
        imMsg.setMagic(ImConstant.DEFAULT_MAGIC);
        imMsg.setCode(code);
        imMsg.setLen(len);
        imMsg.setBody(body);
        out.add(imMsg);
    }
}
