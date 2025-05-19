package com.deqiying.qtool;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Queue;

/**
 * 集合相关工具类
 *
 * @author deqiying
 * @since 2025/2/24
 */
public class CollUtils {
    /**
     * 判断数组是否为空
     *
     * @param array 数组
     * @return 是否为空
     */
    public static boolean isEmpty(Object[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 判断数组是否不为空
     *
     * @param array 数组
     * @return 是否不为空
     */
    public static boolean isNotEmpty(Object[] array) {
        return !isEmpty(array);
    }

    /**
     * 判断集合是否为空
     *
     * @param collection 集合
     * @return 是否为空
     */
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * 判断集合是否不为空
     *
     * @param collection 集合
     * @return 是否不为空
     */
    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    /**
     * 判断Map是否为空
     *
     * @param map Map
     * @return 是否为空
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    /**
     * 判断Map是否不为空
     *
     * @param map Map
     * @return 是否不为空
     */
    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    /**
     * 判断队列是否为空
     *
     * @param queue 队列
     * @return 是否为空
     */
    public static boolean isNotEmpty(Queue<?> queue) {
        return !isEmpty(queue);
    }

    /**
     * 判断队列是否为空
     *
     * @param queue 队列
     * @return 是否为空
     */
    public static boolean isEmpty(Queue<?> queue) {
        return queue == null || queue.isEmpty();
    }

    /**
     * 新增元素，并且忽略空的元素
     *
     * @param collection 集合
     * @param object     元素
     * @param <T>        泛型
     * @return 是否新增成功
     */
    public static <T> boolean addIgnoreNull(final Collection<T> collection, final T object) {
        if (collection == null) {
            throw new NullPointerException("The collection must not be null");
        }
        return object != null && collection.add(object);
    }

    /**
     * 返回一个不为null的集合，如果集合本身不为null，则返回集合本身
     *
     * @param collection 集合
     * @param <T>        泛型
     * @return 集合
     */
    public static <T> Collection<T> emptyIfNull(final Collection<T> collection) {
        return collection == null ? Collections.emptyList() : collection;
    }
}
