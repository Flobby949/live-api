package top.flobby.live.common.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author : Flobby
 * @program : live-api
 * @description : list
 * @create : 2024-01-07 14:18
 **/

public class ListUtils {

    /**
     * 将一个大的list拆分成多个小的list
     *
     * @param list      列表
     * @param spiltSize 长度
     * @return {@link List}<{@link List}<{@link T}>>
     */
    public static <T> List<List<T>> spiltList(List<T> list, int spiltSize) {
        if (list == null || list.isEmpty() || spiltSize < 1) {
            return Collections.emptyList();
        }
        List<List<T>> resultList = new ArrayList<>();
        int listSize = list.size();
        int priIndex = 0;
        int lastIndex = 0;
        int insertTimes = listSize / spiltSize;
        List<T> subList;
        for (int i = 0; i <= insertTimes; i++) {
            priIndex = i * spiltSize;
            lastIndex = priIndex + spiltSize;
            if (i != insertTimes) {
                // 不是最后一次
                subList = list.subList(priIndex, lastIndex);
            } else {
                // 最后一次
                subList = list.subList(priIndex, listSize);
            }
            if (!subList.isEmpty()) {
                resultList.add(subList);
            }
        }
        return resultList;
    }
}
