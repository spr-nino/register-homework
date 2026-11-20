package com.dino.registermodule.utils;

/**
 * 字符串工具类：对 Spring 的 StringUtils 做简单封装。
 */
public final class StringUtils {
    /**
     * 判断字符串是否为空（null 或空串）。
     *
     * @param value 待判断的字符串
     * @return 为空返回 true，否则返回 false
     */
    public static boolean isBlank(String value){
        return org.springframework.util.StringUtils.isEmpty(value);
    }
}
