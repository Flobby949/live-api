package top.flobby.live.user.utils;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 标签工具类
 * @create : 2023-11-28 12:41
 **/

public class TagInfoUtils {

    /**
     * 判断标签是否包含目标标签
     *
     * @param tagInfo  用户当前拥有的标签
     * @param matchTag 被查询的标签
     * @return boolean
     */
    public static boolean isContain(Long tagInfo, Long matchTag) {
        return tagInfo != null && matchTag != null && matchTag > 0 && (tagInfo & matchTag) == matchTag;
    }

    // public static void main(String[] args) {
    //     // 011 , 010
    //     System.out.println(isContain(3L, 2L));
    //     // 011 , 001
    //     System.out.println(isContain(3L, 1L));
    //     // 011 , 100
    //     System.out.println(isContain(3L, 4L));
    //     // 011 , 1000
    //     System.out.println(isContain(3L, 8L));
    // }
}
