package top.flobby.live.id.generate.service;

/**
 * @author : Flobby
 * @program : live-api
 * @description :
 * @create : 2023-11-20 11:42
 **/

public interface IdGenerateService {
    /**
     * 获取 有序 ID
     *
     * @param id 编号
     * @return {@link Long}
     */
    Long getSeqId(Integer id);

    /**
     * 获取 无序 ID
     *
     * @param id 编号
     * @return {@link Long}
     */
    Long getUnSeqId(Integer id);
}
