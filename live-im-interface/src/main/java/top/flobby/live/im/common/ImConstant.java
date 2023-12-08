package top.flobby.live.im.common;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 常量
 * @create : 2023-12-07 15:18
 **/

public interface ImConstant {

    /**
     * 默认魔数
     */
    short DEFAULT_MAGIC = 18673;

    /**
     * ImMsg 最低基本长度，short类型的magic + int类型的code + int类型的len
     */
    int BASE_LENGTH = 2 + 4 + 4;

    /**
     * 心跳时间间隔
     * 30s
     */
    int HEART_BEAT_TIME = 30;

}
