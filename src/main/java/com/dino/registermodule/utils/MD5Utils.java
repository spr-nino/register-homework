package com.dino.registermodule.utils;


import org.apache.tomcat.util.codec.binary.Base64;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5 加密工具类：提供字符串的 MD5 十六进制摘要与 MD5+Base64 摘要。
 *
 * @author xiewanqiang
 */
public class MD5Utils {

    /**
     * 简单测试入口，打印 "123456" 的 MD5 值。
     *
     * @param args 命令行参数（未使用）
     */
    public static void main(String[] args) {
        System.out.println(MD5Utils.hash("123456"));
        // System.out.println(MD5Utils.getInstance().getFileHash("E:\\software\\2003_Visio_Pro.iso"));
    }

    /**
     * 计算字符串的 MD5 摘要，以 32 位小写十六进制字符串返回。
     *
     * @param source 待加密字符串，按 UTF-8 编码
     * @return MD5 十六进制字符串；算法或编码不支持时返回 null
     */
    public static String hash(String source) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            return toHexString(md5.digest(source.getBytes("UTF-8")));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 计算字符串的 MD5 摘要并做 Base64 编码。
     *
     * @param str 待加密字符串，按 UTF-8 编码
     * @return Base64 编码后的 MD5 摘要
     * @throws IllegalArgumentException 加密或编码失败时抛出
     */
    public static String md5Base64(String str) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] buf = md5.digest(str.getBytes("UTF-8"));
            return new String(Base64.encodeBase64(buf));
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("argument [" + str + "] can't be md5 and base64");
        }
    }

    /**
     * 将字节数组转换为小写十六进制字符串。
     *
     * @param b 待转换的字节数组
     * @return 十六进制字符串
     */
    private static String toHexString(byte[] b) {
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            sb.append(hexChar[(b[i] & 0xf0) >>> 4]);
            sb.append(hexChar[b[i] & 0x0f]);
        }
        return sb.toString();
    }

    /** 十六进制字符表（小写） */
    private static char[] hexChar = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
}