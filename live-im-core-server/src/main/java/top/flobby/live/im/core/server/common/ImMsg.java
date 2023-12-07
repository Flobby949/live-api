package top.flobby.live.im.core.server.common;

import lombok.Data;
import top.flobby.live.im.common.ImConstant;
import top.flobby.live.im.common.ImMsgCodeEnum;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 中间消息体
 * @create : 2023-12-07 15:09
 **/


@Data
public class ImMsg implements Serializable {

    @Serial
    private static final long serialVersionUID = -4445525711162970396L;

    /**
     * 魔数，用于基本校验
     */
    private short magic;

    /**
     * 用于标识当前消息类型，后续交给不同的handler处理
     */
    private int code;

    /**
     * 用于记录body的长度
     */
    private int len;

    /**
     * 存储业务消息体。以字节数组形式
     */
    private byte[] body;

    public static ImMsg build(int code, String data) {
        ImMsg imMsg = new ImMsg();
        imMsg.setMagic(ImConstant.DEFAULT_MAGIC);
        imMsg.setCode(code);
        imMsg.setLen(data.getBytes().length);
        imMsg.setBody(data.getBytes());
        return imMsg;
    }

    public static ImMsg build(ImMsgCodeEnum codeEnum, String data) {
        return build(codeEnum.getCode(), data);
    }
}
