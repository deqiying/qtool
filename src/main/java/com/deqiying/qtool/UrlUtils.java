package com.deqiying.qtool;

import com.deqiying.qtool.string.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
public class UrlUtils {
    // URL正则表达式模式，用于解析URL组件
    private static final Pattern URL_PATTERN = Pattern.compile("^(([^:/?#]+):)?(//([^/?#]*))?([^?#]*)(\\?([^#]*))?(#(.*))?");

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
     * @throws MalformedURLException 如果URL格式无效
     */
    private static URI createEncodedUri(String urlStr) throws MalformedURLException {
        if (urlStr == null || urlStr.trim().isEmpty()) {
            throw new MalformedURLException("URL不能为空");
        }

        try {
            // 使用预编译的正则表达式模式
            Matcher matcher = URL_PATTERN.matcher(urlStr);
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

            // 只在需要时进行编码
            String encodedPath = path != null && !path.isEmpty() ? encodePath(path) : "";
            String encodedQuery = query != null && !query.isEmpty() ? encodeQuery(query) : null;

            return new URI(scheme, authority, encodedPath, encodedQuery, fragment);
        } catch (URISyntaxException e) {
            throw new MalformedURLException("处理后的URL无效: " + e.getMessage());
        } catch (UnsupportedEncodingException e) {
            throw new MalformedURLException("编码URL时出错: " + e.getMessage());
        }
    }

    private static String encodeUrlIfNeeded(String url) {
        // 检查是否包含非ASCII字符或空格
        Pattern pattern = Pattern.compile("[^\\x00-\\x7F]|\\s");
        Matcher matcher = pattern.matcher(url);
        if (!matcher.find()) {
            return url; // 无需编码，直接返回原始URL
        }

        // 分割URL，保留协议和路径结构
        try {
            StringBuilder encodedUrl = new StringBuilder();
            String[] parts = url.split("/", -1);
            for (int i = 0; i < parts.length; i++) {
                if (i == 0) {
                    // 保留协议部分（如https:）
                    encodedUrl.append(parts[i]);
                } else {
                    // 对路径部分进行编码
                    String encodedPart = URLEncoder.encode(parts[i], StandardCharsets.UTF_8.name())
                            .replace("+", "%20"); // 将空格编码为%20
                    encodedUrl.append("/").append(encodedPart);
                }
            }
            return encodedUrl.toString();
        } catch (Exception e) {
            // 如果编码失败，返回原始URL
            return url;
        }
    }

    /**
     * 编码URL路径
     *
     * @param path 要编码的路径
     * @return 编码后的路径
     * @throws UnsupportedEncodingException 如果编码失败
     */
    private static String encodePath(String path) throws UnsupportedEncodingException {
        if (path == null || path.isEmpty()) {
            return "";
        }

        // 使用StringBuilder提高性能
        StringBuilder result = new StringBuilder(path.length() * 2);
        String[] segments = path.split("/", -1);

        for (int i = 0; i < segments.length; i++) {
            if (i > 0) {
                result.append('/');
            }

            String segment = segments[i];
            if (!segment.isEmpty()) {
                // 使用StandardCharsets.UTF_8.name()而不是硬编码"UTF-8"
                result.append(URLEncoder.encode(segment, StandardCharsets.UTF_8.name())
                        .replace("+", "%20"));
            }
        }

        return result.toString();
    }

    /**
     * 编码URL查询参数
     *
     * @param query 要编码的查询参数
     * @return 编码后的查询参数，如果query为null或空则返回null
     * @throws UnsupportedEncodingException 如果编码失败
     */
    private static String encodeQuery(String query) throws UnsupportedEncodingException {
        if (query == null || query.isEmpty()) {
            return null;
        }

        String[] params = query.split("&");
        if (params.length == 0) {
            return "";
        }

        // 使用StringBuilder提高性能
        StringBuilder result = new StringBuilder(query.length() * 2);

        for (int i = 0; i < params.length; i++) {
            if (i > 0) {
                result.append('&');
            }

            String param = params[i];
            String[] keyValue = param.split("=", 2);

            // 编码键
            String encodedKey = URLEncoder.encode(keyValue[0], StandardCharsets.UTF_8.name());
            result.append(encodedKey);

            // 如果有值，则添加等号和编码后的值
            if (keyValue.length > 1) {
                result.append('=');
                result.append(URLEncoder.encode(keyValue[1], StandardCharsets.UTF_8.name()));
            } else {
                result.append('=');
            }
        }

        return result.toString();
    }

    /**
     * 获取短链接重定向之后的最终URL
     *
     * @param shortUrl 短链接URL
     * @return 最终重定向后的URL
     * @throws Exception 如果URL无效或请求失败
     */
    public static String getFinalUrl(String shortUrl) throws Exception {
        if (StringUtils.isBlank(shortUrl)) {
            return null;
        }
        RedirectResult finalUrlWithDetails = getFinalUrlWithDetails(encodeUrlIfNeeded(shortUrl));
        return finalUrlWithDetails != null ? finalUrlWithDetails.finalUrl : null;
    }

    /**
     * 获取短链接重定向之后的最终URL（带详细信息）
     *
     * @param shortUrl 短链接URL
     * @return RedirectResult 包含最终URL和重定向信息的结果对象
     * @throws Exception 如果URL无效或请求失败
     */
    public static RedirectResult getFinalUrlWithDetails(String shortUrl) throws Exception {
        if (shortUrl == null || shortUrl.trim().isEmpty()) {
            throw new IllegalArgumentException("URL不能为空");
        }

        RedirectResult result = new RedirectResult();
        result.originalUrl = shortUrl;
        result.redirectChain = new java.util.ArrayList<>();

        String currentUrl = shortUrl;
        int maxRedirects = 10;
        int redirectCount = 0;

        while (redirectCount < maxRedirects) {
            HttpURLConnection connection = null;
            try {
                URI uri = createEncodedUri(currentUrl);
                URL url = uri.toURL();
                connection = (HttpURLConnection) url.openConnection();

                connection.setRequestMethod("HEAD");
                connection.setConnectTimeout(10 * 1000);
                connection.setReadTimeout(10 * 1000);
                connection.setInstanceFollowRedirects(false);
                connection.setRequestProperty("User-Agent",
                        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");

                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_MOVED_PERM ||
                        responseCode == HttpURLConnection.HTTP_MOVED_TEMP ||
                        responseCode == HttpURLConnection.HTTP_SEE_OTHER ||
                        responseCode == 307 ||
                        responseCode == 308) {

                    String location = connection.getHeaderField("Location");
                    if (location == null || location.trim().isEmpty()) {
                        result.finalUrl = currentUrl;
                        result.redirectCount = redirectCount;
                        return result;
                    }

                    // 记录重定向信息
                    RedirectInfo redirectInfo = new RedirectInfo();
                    redirectInfo.fromUrl = currentUrl;
                    redirectInfo.toUrl = location;
                    redirectInfo.statusCode = responseCode;
                    result.redirectChain.add(redirectInfo);

                    // 处理相对URL
                    if (location.startsWith("/")) {
                        URI currentUri = new URI(currentUrl);
                        location = currentUri.getScheme() + "://" + currentUri.getAuthority() + location;
                    } else if (!location.startsWith("http://") && !location.startsWith("https://")) {
                        URI currentUri = new URI(currentUrl);
                        URI locationUri = currentUri.resolve(location);
                        location = locationUri.toString();
                    }

                    currentUrl = location;
                    redirectCount++;
                } else if (responseCode >= 200 && responseCode < 300) {
                    result.finalUrl = currentUrl;
                    result.redirectCount = redirectCount;
                    return result;
                } else {
                    throw new Exception("获取最终URL失败，HTTP状态码: " + responseCode);
                }

            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }

        return null;
    }

    /**
     * 重定向结果类
     */
    public static class RedirectResult {
        public String originalUrl;
        public String finalUrl;
        public int redirectCount;
        public java.util.List<RedirectInfo> redirectChain;

        @Override
        public String toString() {
            return "RedirectResult{" +
                    "originalUrl='" + originalUrl + '\'' +
                    ", finalUrl='" + finalUrl + '\'' +
                    ", redirectCount=" + redirectCount +
                    ", redirectChain=" + redirectChain +
                    '}';
        }
    }

    /**
     * 重定向信息类
     */
    public static class RedirectInfo {
        public String fromUrl;
        public String toUrl;
        public int statusCode;

        @Override
        public String toString() {
            return "RedirectInfo{" +
                    "fromUrl='" + fromUrl + '\'' +
                    ", toUrl='" + toUrl + '\'' +
                    ", statusCode=" + statusCode +
                    '}';
        }
    }
    
    /**
     * 获取URL的协议（scheme），例如 http、https
     *
     * @param urlStr 原始URL字符串
     * @return 协议字符串，无法解析时返回null
     */
    public static String getScheme(String urlStr) {
        try {
            URI uri = createEncodedUri(urlStr);
            return uri.getScheme();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取URL的主机名（host）
     *
     * @param urlStr 原始URL字符串
     * @return 主机名，无法解析时返回null
     */
    public static String getHost(String urlStr) {
        try {
            URI uri = createEncodedUri(urlStr);
            return uri.getHost();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取URL的端口，如果未显式指定返回-1
     *
     * @param urlStr 原始URL字符串
     * @return 端口号，未指定时返回-1，解析失败时返回-1
     */
    public static int getPort(String urlStr) {
        try {
            URI uri = createEncodedUri(urlStr);
            int port = uri.getPort();
            if (port != -1) {
                return port;
            }
            // 未指定端口，按协议返回默认端口
            String scheme = uri.getScheme();
            if ("http".equalsIgnoreCase(scheme)) return 80;
            if ("https".equalsIgnoreCase(scheme)) return 443;
            return -1;
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * 获取URL的路径（path）
     *
     * @param urlStr 原始URL字符串
     * @return 路径字符串，可能为空字符串，解析失败时返回null
     */
    public static String getPath(String urlStr) {
        try {
            URI uri = createEncodedUri(urlStr);
            String p = uri.getPath();
            return p == null ? "" : p;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取基础URL（不含路径、查询与片段）形如：scheme://authority
     *
     * @param urlStr 原始URL字符串
     * @return 基础URL，解析失败时返回null
     */
    public static String getBaseUrl(String urlStr) {
        try {
            URI uri = createEncodedUri(urlStr);
            if (uri.getScheme() == null || uri.getRawAuthority() == null) return null;
            return uri.getScheme() + "://" + uri.getRawAuthority();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 解析URL的查询参数为有序Map（只保留每个key的第一个值）
     *
     * @param urlStr 原始URL字符串
     * @return 参数Map，解析失败或无参数时返回空Map
     */
    public static java.util.Map<String, String> getQueryParams(String urlStr) {
        java.util.Map<String, String> map = new java.util.LinkedHashMap<>();
        try {
            URI uri = createEncodedUri(urlStr);
            String query = uri.getRawQuery();
            if (query == null || query.isEmpty()) return map;
            String[] parts = query.split("&");
            for (String part : parts) {
                if (part.isEmpty()) continue;
                String[] kv = part.split("=", 2);
                String k = URLDecoder.decode(kv[0], java.nio.charset.StandardCharsets.UTF_8.name());
                String v = kv.length > 1 ? URLDecoder.decode(kv[1], java.nio.charset.StandardCharsets.UTF_8.name()) : "";
                // 只保留第一个值
                map.putIfAbsent(k, v);
            }
        } catch (Exception ignored) {
        }
        return map;
    }

    /**
     * 获取URL中指定名称的查询参数的第一个值
     *
     * @param urlStr 原始URL字符串
     * @param name   参数名
     * @return 参数值，未找到或解析失败时返回null
     */
    public static String getQueryParam(String urlStr, String name) {
        if (name == null) return null;
        java.util.Map<String, String> params = getQueryParams(urlStr);
        return params.get(name);
    }

    /**
     * 在URL上新增或替换一个查询参数
     *
     * @param urlStr 原始URL字符串
     * @param name   参数名
     * @param value  参数值，为null时视为空字符串
     * @return 新的URL字符串；解析失败时返回原始URL
     */
    public static String addOrReplaceQueryParam(String urlStr, String name, String value) {
        if (StringUtils.isBlank(urlStr) || StringUtils.isBlank(name)) return urlStr;
        try {
            URI uri = createEncodedUri(urlStr);
            java.util.Map<String, String> params = getQueryParams(urlStr);
            params.put(name, value == null ? "" : value);
            String newQuery = buildQueryString(params);
            URI newUri = new URI(uri.getScheme(), uri.getRawAuthority(), uri.getRawPath(), newQuery, uri.getRawFragment());
            return newUri.toASCIIString();
        } catch (Exception e) {
            return urlStr;
        }
    }

    /**
     * 从URL中移除一个查询参数
     *
     * @param urlStr 原始URL字符串
     * @param name   参数名
     * @return 移除后的URL；解析失败时返回原始URL
     */
    public static String removeQueryParam(String urlStr, String name) {
        if (StringUtils.isBlank(urlStr) || StringUtils.isBlank(name)) return urlStr;
        try {
            URI uri = createEncodedUri(urlStr);
            java.util.Map<String, String> params = getQueryParams(urlStr);
            params.remove(name);
            String newQuery = buildQueryString(params);
            URI newUri = new URI(uri.getScheme(), uri.getRawAuthority(), uri.getRawPath(), newQuery, uri.getRawFragment());
            return newUri.toASCIIString();
        } catch (Exception e) {
            return urlStr;
        }
    }

    /**
     * 通过各部分构建URL
     *
     * @param scheme   协议（必填）
     * @param host     主机（必填）
     * @param port     端口，小于0表示不指定
     * @param path     路径，允许为null或不以/开头
     * @param query    查询参数（key->value），允许为null
     * @param fragment 片段（#后内容），允许为null
     * @return 构建好的URL字符串，参数不合法时返回null
     */
    public static String buildUrl(String scheme, String host, int port, String path, java.util.Map<String, String> query, String fragment) {
        try {
            if (StringUtils.isBlank(scheme) || StringUtils.isBlank(host)) return null;
            String normPath = (path == null || path.isEmpty()) ? null : (path.startsWith("/") ? path : "/" + path);
            String authority = port > -1 ? host + ":" + port : host;
            String queryStr = buildQueryString(query);
            URI uri = new URI(scheme, authority, normPath, queryStr, fragment);
            return uri.toASCIIString();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 规范化URL：
     * 1) 解决相对路径中的".."与"."；2) 压缩多余的斜杠；3) 正确进行编码
     *
     * @param urlStr 原始URL
     * @return 规范化后的URL，失败时返回原始URL
     */
    public static String normalizeUrl(String urlStr) {
        if (StringUtils.isBlank(urlStr)) return urlStr;
        try {
            URI uri = createEncodedUri(urlStr);
            URI normalized = new URI(uri.getScheme(), uri.getRawAuthority(), uri.normalize().getRawPath(), uri.getRawQuery(), uri.getRawFragment());
            // 去除路径中重复的斜杠（但保留协议后的双斜杠）
            String s = normalized.toASCIIString();
            int idx = s.indexOf("://");
            String head = idx > -1 ? s.substring(0, idx + 3) : "";
            String rest = idx > -1 ? s.substring(idx + 3) : s;
            rest = rest.replaceAll("/{2,}", "/");
            return head + rest;
        } catch (Exception e) {
            return urlStr;
        }
    }

    /**
     * 解析相对地址，基于baseUrl得到绝对URL
     *
     * @param baseUrl 基础URL
     * @param relative 相对路径或URL
     * @return 解析后的绝对URL；失败时返回relative本身
     */
    public static String resolve(String baseUrl, String relative) {
        if (StringUtils.isBlank(relative)) return relative;
        try {
            if (StringUtils.isBlank(baseUrl)) return createEncodedUri(relative).toASCIIString();
            URI base = createEncodedUri(baseUrl);
            URI rel = new URI(relative);
            return base.resolve(rel).toASCIIString();
        } catch (Exception e) {
            return relative;
        }
    }

    // 构建查询字符串（内部使用）
    private static String buildQueryString(java.util.Map<String, String> params) throws UnsupportedEncodingException {
        if (params == null || params.isEmpty()) return null;
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (java.util.Map.Entry<String, String> e : params.entrySet()) {
            if (!first) sb.append('&');
            first = false;
            String k = URLEncoder.encode(e.getKey(), java.nio.charset.StandardCharsets.UTF_8.name());
            String v = e.getValue() == null ? "" : URLEncoder.encode(e.getValue(), java.nio.charset.StandardCharsets.UTF_8.name());
            sb.append(k).append('=').append(v);
        }
        return sb.toString();
    }

}