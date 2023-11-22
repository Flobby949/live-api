package top.flobby.live.id.generate.service.bo;

import lombok.Data;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 无序Id
 * @create : 2023-11-20 11:45
 **/

@Data
public class LocalUnSeqBO {

    /**
     * 业务id
     */
    private int id;
    /**
     * 内存当中记录的无序id值
     */
    private ConcurrentLinkedQueue<Long> idQueue;
    /**
     * 当前id的开始值
     */
    private long currentStart;
    /**
     * 当前id的结束值
     */
    private long nextThreshold;
}
