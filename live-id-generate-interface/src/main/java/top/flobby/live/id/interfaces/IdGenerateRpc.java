package top.flobby.live.id.interfaces;

/**
 * @author : Flobby
 * @program : live-api
 * @description : rpc
 * @create : 2023-11-20 11:10
 **/

public interface IdGenerateRpc {

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
