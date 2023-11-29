package top.flobby.live.user.constants;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 常量
 * @create : 2023-11-28 12:36
 **/

public interface Constant {

    /**
     * 数据库字段常量
     */
    // userId 字段
    String USER_ID = "user_id";
    // tag_info 字段
    String TAG_INFO_01 = "tag_info_01";
    String TAG_INFO_02 = "tag_info_02";
    String TAG_INFO_03 = "tag_info_03";

    /**
     * 专门处理用户缓存异步删除的 topic
     */
    String CACHE_ASYNC_DELETE = "UserCacheAsyncDelete";
}
