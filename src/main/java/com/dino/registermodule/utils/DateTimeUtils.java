package com.dino.registermodule.utils;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

/**
 * 日期时间工具类：提供日期格式化、月份首尾时间计算，以及字符串与 Date/LocalDate 之间的转换。
 */
public final class DateTimeUtils {
    /** 秒级精度日期时间格式：yyyy-MM-dd HH:mm:ss */
    public static String DATE_FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    /** 毫秒级紧凑日期时间格式：yyyyMMddHHmmssSSS */
    public static String DATE_FORMAT_YYYYMMDDHHMMSSSSS = "yyyyMMddHHmmssSSS";

    /**
     * java8(经测试别的版本获取2月有bug) 获取某月第一天的00:00:00
     *
     * @param datestr 形如 "yyyy-MM" 的年月字符串
     * @return 该月第一天 00:00:00 的格式化字符串；datestr 为空白时返回 null
     */
    public static String getFirstDayOfMonth(String datestr) {
        if (StringUtils.isBlank(datestr)) return null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = strToDateNotDD(datestr);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());
        ;
        LocalDateTime endOfDay = localDateTime.with(TemporalAdjusters.firstDayOfMonth()).with(LocalTime.MIN);
        Date dates = Date.from(endOfDay.atZone(ZoneId.systemDefault()).toInstant());
        return sdf.format(dates);
    }

    /**
     * java8(别的版本获取2月有bug) 获取某月最后一天的23:59:59
     *
     * @param datestr 形如 "yyyy-MM" 的年月字符串
     * @return 该月最后一天 23:59:59 的格式化字符串；datestr 为空白时返回 null
     */
    public static String getLastDayOfMonth(String datestr) {
        if (StringUtils.isBlank(datestr)) return null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = strToDateNotDD(datestr);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());
        ;
        LocalDateTime endOfDay = localDateTime.with(TemporalAdjusters.lastDayOfMonth()).with(LocalTime.MAX);
        Date dates = Date.from(endOfDay.atZone(ZoneId.systemDefault()).toInstant());
        return sdf.format(dates);
    }

    /**
     * 将短时间格式字符串转换为时间 yyyy-MM( 2017-02)
     *
     * @param strDate 形如 "yyyy-MM" 的年月字符串
     * @return 解析得到的 Date；strDate 为空白或解析失败时返回 null
     */
    public static Date strToDateNotDD(String strDate) {
        if (StringUtils.isBlank(strDate)) return null;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }

    /**
     * 按指定格式将日期时间字符串解析为 Date（使用系统默认时区）。
     *
     * @param strDate       待解析的日期时间字符串
     * @param dateFormatter 与字符串匹配的日期时间格式，如 "yyyy-MM-dd HH:mm:ss"
     * @return 解析得到的 Date
     */
    public static Date stringToDate(String strDate, String dateFormatter) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(dateFormatter);
        LocalDateTime localDateTime = LocalDateTime.parse(strDate, dateTimeFormatter);
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        Date date = Date.from(instant);
        return date;
    }

    /**
     * 按指定格式获取当前时间的字符串表示。
     *
     * @param dateFormatter 目标日期时间格式
     * @return 格式化后的当前时间字符串
     */
    public static String getDateTimeFormatter(String dateFormatter) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(dateFormatter);
        LocalDateTime localDateTime = LocalDateTime.now();
        String stringDate = dateTimeFormatter.format(localDateTime);
        return stringDate;
    }


    /**
     * 获取当前日期。
     *
     * @return 系统默认时区下的当前日期（LocalDate）
     */
    public static LocalDate getCurrentDate() {
        return LocalDate.now();
    }

    /**
     * 获取当前时间戳。
     *
     * @return 当前时间的毫秒时间戳
     */
    public static long getCurrentTimeMillis() {
        return System.currentTimeMillis();
    }

}
