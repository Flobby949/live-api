package top.flobby.live.user.utils;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 公共工具类
 * @create : 2023-11-29 15:24
 **/

public class CommonUtils {

    /**
     * 创建Redis随机过期时间
     * 30 min + 随机秒数
     *
     * @return int
     */
    public static int createRandomExpireTime() {
        return ThreadLocalRandom.current().nextInt(1000) + 60 * 30;
    }
}
