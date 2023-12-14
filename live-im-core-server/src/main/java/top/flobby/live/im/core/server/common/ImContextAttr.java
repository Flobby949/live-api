package top.flobby.live.im.core.server.common;

import io.netty.util.AttributeKey;

/**
 * @author : Flobby
 * @program : live-api
 * @description : ctx 属性
 * @create : 2023-12-07 18:01
 **/


public class ImContextAttr {
    /**
     * 用户 ID
     */
    public static final AttributeKey<Long> USER_ID = AttributeKey.valueOf("userId");

    /**
     * 应用 ID
     */
    public static final AttributeKey<Integer> APP_ID = AttributeKey.valueOf("appId");


    public static final AttributeKey<Long> ROOM_ID = AttributeKey.valueOf("roomId");
}
