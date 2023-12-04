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


    /// 用户状态
    // 有效
    Byte USER_STATUS_EFFECTIVE = 0;
    // 无效
    Byte USER_STATUS_INVALID = 1;

    /// token payload
    // 用户 ID
    String TOKEN_USER_ID = "userId";

    // 用户默认头像
    String DEFAULT_AVATAR = "https://i2.100024.xyz/2023/12/04/po4rph.webp";
}
