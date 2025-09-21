package com.deqiying.qtool.string;

import java.util.Arrays;

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
     * 填充长度限制
     */
    private static final int PAD_LIMIT = 8192;

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
     * 获取左边指定长度的字符串,不足的用指定字符填充
     *
     * @param str      字符串
     * @param len      长度
     * @param fillChar 填充字符
     * @return 左边指定长度的字符串
     */
    public static String left(final String str, final int len, final char fillChar) {
        if (str == null) {
            return null;
        }
        if (len < 0) {
            return EMPTY;
        }
        if (str.length() <= len) {
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            while (sb.length() < len) {
                sb.append(fillChar);
            }
            return sb.toString();

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

    /**
     * 获取右边指定长度的字符串,不足的用指定字符填充
     *
     * @param str      字符串
     * @param len      长度
     * @param fillChar 填充字符
     * @return 右边指定长度的字符串
     */
    public static String right(final String str, final int len, final char fillChar) {
        if (str == null) {
            return null;
        }
        if (len < 0) {
            return EMPTY;
        }
        if (str.length() <= len) {
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            while (sb.length() < len) {
                sb.insert(0, fillChar);
            }
            return sb.toString();
        }
        return str.substring(str.length() - len);
    }

    /**
     * 判断字符串是否不为空
     *
     * @param cs 字符串
     * @return true：不为空，false：为空
     */
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
     * 使用重复为给定长度重复的指定定界符返回填充。
     *
     * @param ch     重复的字符
     * @param repeat 重复的次数
     * @return 重复后的字符串
     */
    public static String repeat(final char ch, final int repeat) {
        if (repeat <= 0) {
            return EMPTY;
        }
        final char[] buf = new char[repeat];
        Arrays.fill(buf, ch);
        return new String(buf);
    }

    public static String rightPad(final String str, final int size, final char padChar) {
        if (str == null) {
            return null;
        }
        final int pads = size - str.length();
        if (pads <= 0) {
            return str; // returns original String when possible
        }
        if (pads > PAD_LIMIT) {
            return rightPad(str, size, String.valueOf(padChar));
        }
        return str.concat(repeat(padChar, pads));
    }

    /**
     * 右边填充
     *
     * @param str    字符串
     * @param size   填充后的长度
     * @param padStr 填充的字符串
     * @return 填充后的字符串
     */
    public static String rightPad(final String str, final int size, String padStr) {
        if (str == null) {
            return null;
        }
        if (isEmpty(padStr)) {
            padStr = SPACE;
        }
        final int padLen = padStr.length();
        final int strLen = str.length();
        final int pads = size - strLen;
        if (pads <= 0) {
            return str; // returns original String when possible
        }
        if (padLen == 1 && pads <= PAD_LIMIT) {
            return rightPad(str, size, padStr.charAt(0));
        }

        if (pads == padLen) {
            return str.concat(padStr);
        }
        if (pads < padLen) {
            return str.concat(padStr.substring(0, pads));
        }
        final char[] padding = new char[pads];
        final char[] padChars = padStr.toCharArray();
        for (int i = 0; i < pads; i++) {
            padding[i] = padChars[i % padLen];
        }
        return str.concat(new String(padding));
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

    
    /**
     * 当字符串为null或仅包含空白字符时，返回默认值
     *
     * @param str        原字符串
     * @param defaultStr 默认值
     * @return 非空白时返回str，否则返回defaultStr
     */
    public static String defaultIfBlank(String str, String defaultStr) {
        return isBlank(str) ? defaultStr : str;
    }

    /**
     * 当字符串为null时返回空串，否则原样返回
     *
     * @param str 原字符串
     * @return 非null字符串
     */
    public static String defaultString(String str) {
        return str == null ? EMPTY : str;
    }

    /**
     * null安全的忽略大小写比较
     *
     * @param a 字符序列A
     * @param b 字符序列B
     * @return 是否相等
     */
    public static boolean equalsIgnoreCase(final CharSequence a, final CharSequence b) {
        if (a == b) return true;
        if (a == null || b == null) return false;
        if (a.length() != b.length()) return false;
        for (int i = 0; i < a.length(); i++) {
            if (Character.toLowerCase(a.charAt(i)) != Character.toLowerCase(b.charAt(i))) return false;
        }
        return true;
    }

    /**
     * 判断seq是否包含search
     */
    public static boolean contains(final CharSequence seq, final CharSequence search) {
        if (seq == null || search == null) return false;
        return seq.toString().contains(search);
    }

    /**
     * 判断字符串是否以prefix开头
     */
    public static boolean startsWith(final CharSequence str, final CharSequence prefix) {
        if (str == null || prefix == null) return false;
        return str.toString().startsWith(prefix.toString());
    }

    /**
     * 判断字符串是否以suffix结尾
     */
    public static boolean endsWith(final CharSequence str, final CharSequence suffix) {
        if (str == null || suffix == null) return false;
        return str.toString().endsWith(suffix.toString());
    }

    /**
     * 使用分隔符拼接字符串数组
     *
     * @param delimiter 分隔符
     * @param parts     待拼接元素
     * @return 拼接结果，为空输入时返回空串
     */
    public static String join(final String delimiter, final Object... parts) {
        if (parts == null || parts.length == 0) return EMPTY;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < parts.length; i++) {
            if (i > 0) sb.append(delimiter);
            sb.append(parts[i] == null ? "null" : parts[i].toString());
        }
        return sb.toString();
    }

    /**
     * 简单分割（不保留空项），对每项进行trim
     *
     * @param str       原字符串
     * @param delimiter 分隔符
     * @return 分割后的字符串数组，str为null时返回空数组
     */
    public static String[] split(final String str, final String delimiter) {
        if (str == null) return new String[0];
        if (delimiter == null || delimiter.isEmpty()) return new String[]{str};
        String[] arr = str.split(java.util.regex.Pattern.quote(delimiter));
        java.util.List<String> list = new java.util.ArrayList<>(arr.length);
        for (String s : arr) {
            if (s == null) continue;
            String t = s.trim();
            if (!t.isEmpty()) list.add(t);
        }
        return list.toArray(new String[0]);
    }

    /**
     * 首字母大写
     */
    public static String capitalize(final String str) {
        if (isBlank(str)) return str;
        char first = str.charAt(0);
        char up = Character.toUpperCase(first);
        if (first == up) return str;
        return up + str.substring(1);
    }

    /**
     * 首字母小写
     */
    public static String uncapitalize(final String str) {
        if (isBlank(str)) return str;
        char first = str.charAt(0);
        char low = Character.toLowerCase(first);
        if (first == low) return str;
        return low + str.substring(1);
    }

    /**
     * 将下划线或短横线风格转为驼峰（user_name、user-name -> userName）
     */
    public static String toCamelCase(final String str) {
        if (isBlank(str)) return str;
        StringBuilder sb = new StringBuilder(str.length());
        boolean upperNext = false;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == '_' || c == '-') {
                upperNext = true;
            } else {
                if (upperNext) {
                    sb.append(Character.toUpperCase(c));
                    upperNext = false;
                } else {
                    sb.append(Character.toLowerCase(c));
                }
            }
        }
        return sb.toString();
    }

    /**
     * 将驼峰转为下划线（userName -> user_name）
     */
    public static String toSnakeCase(final String str) {
        if (isBlank(str)) return str;
        StringBuilder sb = new StringBuilder(str.length() + 8);
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (Character.isUpperCase(c)) {
                if (i > 0) sb.append(SEPARATOR);
                sb.append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * null转空串
     */
    public static String nullToEmpty(final String str) {
        return str == null ? EMPTY : str;
    }

    /**
     * 空串转null（当为空串或全是空白字符时返回null）
     */
    public static String emptyToNull(final String str) {
        return isBlank(str) ? null : str;
    }

}
