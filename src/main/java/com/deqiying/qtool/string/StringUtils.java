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

    /**
     * 获取左边指定长度的字符串
     *
     * @param str 字符串
     * @param len 长度
     * @return 左边指定长度的字符串
     */
    public static String left(final String str, final int len) {
        if (str == null) {
            return null;
        }
        if (len < 0) {
            return EMPTY;
        }
        if (str.length() <= len) {
            return str;
        }
        return str.substring(0, len);
    }

    /**
     * 获取右边指定长度的字符串
     *
     * @param str 字符串
     * @param len 长度
     * @return 右边指定长度的字符串
     */
    public static String right(final String str, final int len) {
        if (str == null) {
            return null;
        }
        if (len < 0) {
            return EMPTY;
        }
        if (str.length() <= len) {
            return str;
        }
        return str.substring(str.length() - len);
    }

    public static boolean isNotEmpty(final CharSequence cs) {
        return !isEmpty(cs);
    }

    /**
     * 截取字符串
     *
     * @param str   字符串
     * @param start 开始位置
     * @param end   结束位置
     * @return 截取后的字符串
     */
    public static String substring(final String str, int start, int end) {
        if (str == null) {
            return null;
        }

        // handle negatives
        if (end < 0) {
            end = str.length() + end; // remember end is negative
        }
        if (start < 0) {
            start = str.length() + start; // remember start is negative
        }

        // check length next
        if (end > str.length()) {
            end = str.length();
        }

        // if start is greater than end, return ""
        if (start > end) {
            return EMPTY;
        }

        if (start < 0) {
            start = 0;
        }
        if (end < 0) {
            end = 0;
        }

        return str.substring(start, end);
    }

    /**
     * 截取字符串
     *
     * @param str       字符串
     * @param separator 分隔符
     * @return 截取后的字符串
     */
    public static String substringAfter(final String str, final int separator) {
        if (isEmpty(str)) {
            return str;
        }
        final int pos = str.indexOf(separator);
        if (pos == INDEX_NOT_FOUND) {
            return EMPTY;
        }
        return str.substring(pos + 1);
    }

    /**
     * 大小写反转
     *
     * @param str 字符串
     * @return 反转后的字符串
     */
    public static String swapCase(final String str) {
        if (isEmpty(str)) {
            return str;
        }

        final int strLen = str.length();
        final int[] newCodePoints = new int[strLen]; // cannot be longer than the char array
        int outOffset = 0;
        for (int i = 0; i < strLen; ) {
            final int oldCodepoint = str.codePointAt(i);
            final int newCodePoint;
            if (Character.isUpperCase(oldCodepoint) || Character.isTitleCase(oldCodepoint)) {
                newCodePoint = Character.toLowerCase(oldCodepoint);
            } else if (Character.isLowerCase(oldCodepoint)) {
                newCodePoint = Character.toUpperCase(oldCodepoint);
            } else {
                newCodePoint = oldCodepoint;
            }
            newCodePoints[outOffset++] = newCodePoint;
            i += Character.charCount(newCodePoint);
        }
        return new String(newCodePoints, 0, outOffset);
    }

}
