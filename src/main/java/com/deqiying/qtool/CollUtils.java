package com.deqiying.qtool;

import java.util.Collection;
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
}
