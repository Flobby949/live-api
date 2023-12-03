package top.flobby.live.msg.provider.config;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 异步短信发送线程池
 * @create : 2023-12-03 13:01
 **/

public class ThreadPoolManager {

    public static ThreadPoolExecutor commonAsyncPool = new ThreadPoolExecutor(
            2,
            8,
            3,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(100),
            r -> {
                Thread newThread = new Thread(r);
                newThread.setName(" commonAsyncPool - " + ThreadLocalRandom.current().nextInt(10000));
                return newThread;

            }
    );
}
