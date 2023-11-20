package top.flobby.live.id.generate.service.bo;

import lombok.Data;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 内存中的id
 * @create : 2023-11-20 11:45
 **/

@Data
public class LocalSeqBO {

    /**
     * 业务id
     */
    private int id;
    /**
     * 内存当中记录的有序id值
     * 使用原子类，保证线程安全
     */
    private AtomicLong currentNum;
    /**
     * 当前id的开始值
     */
    private long currentStart;
    /**
     * 当前id的结束值
     */
    private long nextThreshold;
}
