package com.deqiying.qtool.net;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class IpUtils {

    /**
     * 判断是否为内网IP
     *
     * @param address ip地址
     * @return 是否为内网IP
     */
    public static boolean isPrivateIp(String address) {
        try {
            InetAddress inetAddress = InetAddress.getByName(address);
            return isPrivateIp(inetAddress);
        } catch (UnknownHostException ignored) {
        }
        return false;
    }

    /**
     * 判断是否为内网IP
     *
     * @param inetAddress InetAddress对象
     * @return 是否为内网IP
     */
    public static boolean isPrivateIp(InetAddress inetAddress) {
        // 检查IPv4地址范围
        if (inetAddress instanceof java.net.Inet4Address) {
            return isPrivateIpInet4((Inet4Address) inetAddress);
        }
        // 检查IPv6地址范围
        if (inetAddress instanceof java.net.Inet6Address) {
            return isPrivateIpInet6((Inet6Address) inetAddress);
        }
        return false;
    }

    /**
     * 判断是否为内网IP
     *
     * @param inet4Address Inet4Address对象
     * @return 是否为内网IP
     */
    public static boolean isPrivateIpInet4(Inet4Address inet4Address) {
        byte[] bytes = inet4Address.getAddress();

        // 检查IPv4地址范围
        // 判断是否为10.0.0.0 - 10.255.255.255
        if (bytes[0] == (byte) 10) {
            return true;
        }
        // 判断是否为172.16.0.0 - 172.31.255.255
        if (bytes[0] == (byte) 172 && (bytes[1] >= 16 && bytes[1] <= 31)) {
            return true;
        }
        // 判断是否为192.168.0.0 - 192.168.255.255
        if (bytes[0] == (byte) 192 && bytes[1] == (byte) 168) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否为内网IP
     *
     * @param inet6Address Inet6Address对象
     * @return 是否为内网IP
     */
    public static boolean isPrivateIpInet6(Inet6Address inet6Address) {
        byte[] bytes = inet6Address.getAddress();
        // 判断是否为链路本地地址 (fe80::/10)
        if ((bytes[0] == (byte) 0xfe) && (bytes[1] >= (byte) 0x80 && bytes[1] <= (byte) 0xbf)) {
            return true;
        }
        // 判断是否为唯一本地地址 (fc00::/7)
        if (bytes[0] == (byte) 0xfc || bytes[0] == (byte) 0xfd) {
            return true;
        }
        return false;
    }

}
