package com.deqiying.qtool;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class UrlUtils {
    /**
     * 判断字符串是否是一个有效的URL
     *
     * @param url 要判断的字符串
     * @return 如果是有效URL则返回true，否则返回false
     */
    public static boolean isValidUrl(String url) {
        try {
            new URL(url);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }

    /**
     * 尝试打开一个URL链接，如果响应类型是文件或流则返回InputStream，否则返回null
     *
     * @param url 要打开的URL
     * @return InputStream 如果是文件或流；否则返回null
     * @throws Exception 如果URL无效或请求失败
     */
    public static InputStream openUrl(String url) throws Exception {
        // 打开连接
        HttpURLConnection connection = openUrlConnection(url);

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            return connection.getInputStream();
        } else {
            throw new Exception("无法打开URL,响应码: " + responseCode);
        }
    }

    /**
     * 尝试下载一个URL链接
     *
     * @param url 要打开的URL
     * @return byte[] 下载好的字节流
     * @throws Exception 如果URL无效或请求失败
     */
    public static byte[] downloadUrl(String url) throws Exception {
        try (InputStream inputStream = openUrl(url);
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            // 缓冲区
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            return outputStream.toByteArray();
        }
    }

    /**
     * 尝试打开一个URL链接，返回HttpURLConnection对象
     *
     * @param urlStr 要打开的URL
     * @return HttpURLConnection 如果是文件或流；否则返回null
     * @throws Exception 如果URL无效或请求失败
     */
    public static HttpURLConnection openUrlConnection(String urlStr) throws Exception {
        URI uri = createEncodedUri(urlStr);
        URL url = uri.toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(10 * 60 * 1000);
        connection.setReadTimeout(0);
        return connection;
    }

    /**
     * 创建一个编码后的URI对象
     *
     * @param urlStr 要打开的URL
     * @return URI 编码后的URI对象
     * @throws Exception 如果URL无效或请求失败
     */
    private static URI createEncodedUri(String urlStr) throws Exception {
        Pattern urlPattern = Pattern.compile("^(([^:/?#]+):)?(//([^/?#]*))?([^?#]*)(\\?([^#]*))?(#(.*))?");
        Matcher matcher = urlPattern.matcher(urlStr);
        if (!matcher.find()) {
            throw new MalformedURLException("无效的URL: " + urlStr);
        }

        String scheme = matcher.group(2);
        if (scheme == null) {
            throw new MalformedURLException("URL缺少协议: " + urlStr);
        }

        String authority = matcher.group(4);
        String path = matcher.group(5);
        String query = matcher.group(7);
        String fragment = matcher.group(9);

        String encodedPath = encodePath(path != null ? path : "");
        String encodedQuery = encodeQuery(query);

        try {
            return new URI(scheme, authority, encodedPath, encodedQuery, fragment);
        } catch (URISyntaxException e) {
            throw new MalformedURLException("处理后的URL无效: " + e.getMessage());
        }
    }

    /**
     * 编码URL路径和查询参数
     *
     * @param path 要编码的路径
     * @return 编码后的路径
     * @throws UnsupportedEncodingException 如果编码失败
     */
    private static String encodePath(String path) throws UnsupportedEncodingException {
        if (path.isEmpty()) {
            return path;
        }
        String[] segments = path.split("/", -1);
        for (int i = 0; i < segments.length; i++) {
            String segment = segments[i];
            if (!segment.isEmpty()) {
                String encodedSegment = URLEncoder.encode(segment, StandardCharsets.UTF_8.name())
                        .replace("+", "%20");
                segments[i] = encodedSegment;
            }
        }
        return String.join("/", segments);
    }

    /**
     * 编码URL查询参数
     *
     * @param query 要编码的查询参数
     * @return 编码后的查询参数
     */
    private static String encodeQuery(String query) {
        if (query == null || query.isEmpty()) {
            return null;
        }
        return Arrays.stream(query.split("&"))
                .map(param -> {
                    String[] keyValue = param.split("=", 2);
                    try {
                        String encodedKey = URLEncoder.encode(keyValue[0], StandardCharsets.UTF_8.name());
                        String encodedValue = keyValue.length > 1 ? URLEncoder.encode(keyValue[1], StandardCharsets.UTF_8.name()) : "";
                        return encodedKey + "=" + encodedValue;
                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.joining("&"));
    }


}