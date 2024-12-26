package com.deqiying.qtool.string;

/**
 * 字符串工具类
 *
 * @author deqiying
 */
@SuppressWarnings(value = {"unused"})
public class StringUtils {
    /**
     * 空字符串
     */
    private static final String NULL_STR = "";

    /**
     * 下划线
     */
    private static final char SEPARATOR = '_';

    /**
     * 去空格
     */
    public static String trim(String str) {
        return (str == null ? NULL_STR : str.trim());
    }
}
