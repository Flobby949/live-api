package top.flobby.live.common.utils;

import org.springframework.beans.BeanInstantiationException;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 实体类转换工具
 * @create : 2023-11-18 14:55
 **/

public class ConvertBeanUtils {
    /**
     * 将一个对象转成目标对象
     *
     * @param source      源
     * @param targetClass 目标类
     * @return {@link T}
     */
    public static <T> T convert(Object source, Class<T> targetClass) {
        if (source == null) {
            return null;
        }
        T t = newInstance(targetClass);
        BeanUtils.copyProperties(source, t);
        return t;
    }

    /**
     * 将List对象转换成目标对象，注意实现是ArrayList
     *
     * @param sourceList  源列表
     * @param targetClass 目标类
     * @return {@link List}<{@link T}>
     */
    public static <K, T> List<T> convertList(List<K> sourceList, Class<T> targetClass) {
        if (sourceList == null) {
            return Collections.emptyList();
        }
        List<T> targetList = new ArrayList<>((int) (sourceList.size() / 0.75) + 1);
        for (K source : sourceList) {
            targetList.add(convert(source, targetClass));
        }
        return targetList;
    }

    private static <T> T newInstance(Class<T> targetClass) {
        try {
            return targetClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new BeanInstantiationException(targetClass, "instantiation error", e);
        }
    }

}
