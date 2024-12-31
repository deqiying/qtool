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
    private static final String EMPTY = "";

    /**
     * 空格
     */
    public static final String SPACE = " ";

    /**
     * LF ("\n")
     */
    public static final String LF = "\n";

    /**
     * CR ("\r").
     */
    public static final String CR = "\r";

    /**
     * 下划线
     */
    private static final char SEPARATOR = '_';

    /**
     * 未找到的索引值。
     */
    public static final int INDEX_NOT_FOUND = -1;

    /**
     * 去空格
     */
    public static String trim(String str) {
        return (str == null ? EMPTY : str.trim());
    }

    /**
     * 判断字符串是否为空
     *
     * @param cs 字符串
     * @return true：为空，false：不为空
     */
    public static boolean isEmpty(final CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    /**
     * 判断字符串是否为空
     *
     * @param cs 字符串
     * @return true：为空，false：不为空
     */
    public static boolean isBlank(final CharSequence cs) {
        final int strLen = length(cs);
        if (strLen == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断字符串是否不为空
     *
     * @param cs 字符串
     * @return true：不为空，false：为空
     */
    public static boolean isNotBlank(final CharSequence cs) {
        return !isBlank(cs);
    }

    /**
     * 获取字符串长度
     *
     * @param cs 字符串
     * @return 字符串长度
     */
    public static int length(final CharSequence cs) {
        return cs == null ? 0 : cs.length();
    }
}
