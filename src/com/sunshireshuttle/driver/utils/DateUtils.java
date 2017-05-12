package com.sunshireshuttle.driver.utils;

import android.annotation.SuppressLint;

import net.iaf.framework.util.Loger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.sunshireshuttle.driver.SunDriverApplication;

public final class DateUtils {

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
    private static final SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
    private static final SimpleDateFormat dateFormatTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
    public static final SimpleDateFormat dateFormatTimeHM = new SimpleDateFormat("HH:mm", Locale.getDefault());
    private static final SimpleDateFormat dateFormatShort = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    public static final SimpleDateFormat dateFormatLong = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
            Locale.getDefault());
    private static final SimpleDateFormat dateFormat3 = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    public static final SimpleDateFormat dateFormat4 = new SimpleDateFormat("MM/dd HH:mm", Locale.getDefault());
    private static final SimpleDateFormat dateFormatCard = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
    private static final SimpleDateFormat dataYear = new SimpleDateFormat("yyyy", Locale.getDefault());
    private static final String FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String SIMPLE_FORMAT = "yyyy/MM/dd";

    /**
     * 获取时区信息
     *
     * @return
     */
    public static TimeZone getTimeZone() {
        TimeZone zone = TimeZone.getDefault();
        Loger.d("TimeZone LONG:" + zone.getDisplayName(true, TimeZone.LONG, SunDriverApplication.getContext().getResources().getConfiguration().locale));
        Loger.d("TimeZone Short:" + zone.getDisplayName(true, TimeZone.SHORT, SunDriverApplication.getContext().getResources().getConfiguration().locale));
        return zone;
    }

    /**
     * 将字符串日期转换成指定时区的指定格式的字符串日期
     *
     * @param date     日期字符串，必须为"yyyy-MM-dd HH:mm:ss"
     * @param timeZone 指定的时区
     * @param format   指定的表达形式
     * @return 指定字符串的表达形式
     */
    public static String convertTimeZone(String date, TimeZone timeZone, String format) {
        Date dt = parseDate(date);
        return formatDate(dt, format, timeZone);
    }

    /**
     * 将Date对象转换成指定时区的指定格式的字符串日期
     *
     * @param date     Date对象
     * @param format   指定的日期字符串格式
     * @param timeZone 指定的时区
     * @return 返回指定格式的日期
     */
    public static String formatDate(Date date, String format, TimeZone timeZone) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        dateFormat.setTimeZone(timeZone);
        return dateFormat.format(date);
    }

    public static String formatDate(Date date, SimpleDateFormat format) {
        return format.format(date);
    }

    public static String formatDateStr(String dateStr, SimpleDateFormat formatIn, SimpleDateFormat formatOut) throws ParseException {
        Date date = formatIn.parse(dateStr);
        return formatOut.format(date);
    }


    /**
     * 将Date对象转换为指定格式的字符串
     *
     * @param date Date对象
     * @return Date对象的字符串表达形式"yyyy-MM-dd HH:mm:ss"
     */
    public static String formatDate(Date date) {
        return formatDate(date, FORMAT);
    }

    /**
     * 将日期字符串转换为Date对象
     *
     * @param date 日期字符串，必须为"yyyy-MM-dd HH:mm:ss"
     * @return 日期字符串的Date对象表达形式
     */
    public static Date parseDate(String date) {
        return parseDate(date, FORMAT);
    }

    /**
     * 将日期字符串转换为Date对象
     *
     * @param date   日期字符串，必须为"yyyy-MM-dd HH:mm:ss"
     * @param format 格式化字符串
     * @return 日期字符串的Date对象表达形式
     */
    public static Date parseDate(String date, String format) {
        Date dt = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        try {
            dt = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dt;
    }

    public static Date parseDate(String date, SimpleDateFormat format) {
        Date dt = null;
        try {
            dt = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dt;
    }

    public static String getDateCardTime(String date) throws ParseException {
        Date date2 = dateFormat.parse(date);
        String str = dateFormatCard.format(date2);
        return str;
    }

    public static String getDateTime3() {
        String str = dateFormat3.format(new Date());
        return str;
    }

    public static String getDateTime(String date) throws ParseException {
        Date date2 = dateFormat.parse(date);
        String str = dateFormatTime.format(date2);
        return str;
    }

    public static String getDateDay(Date date) {
        String str = dateFormatShort.format(date);
        return str;
    }

    public static String getDateDay(String date) throws ParseException {
        Date date2 = dateFormat2.parse(date);
        String str = dateFormatShort.format(date2);
        return str;
    }

    public static String getBATDateDay(String date) throws ParseException {
        String str = dateFormatLong.format(date);
        return str;
    }

    public static String getLongDate(String date) throws ParseException {
        Date date2 = dateFormat.parse(date);
        String str = dateFormatLong.format(date2);
        return str;
    }

//	public static String getBATDate(String date) throws ParseException  {
//			Loger.d("date"+date);
////			String str = dateFormatLong.parse(date);
////			Date date2 = dateFormatLong.parse(date);
////			String str = dateFormatLong.format(date2);
//			return date;
//	}

    public static String getShortDate(String date) throws ParseException {
        Date date2 = dateFormat.parse(date);
        String str = dateFormatShort.format(date2);
        return str;
    }

    /**
     * @param dateFormat
     * @return yyyyMMddHHmmss
     */
    public static String getDate(SimpleDateFormat dateFormat) {
        Date date = new Date();
        String str = dateFormat.format(date);
        // TimeZone timezone = TimeZone.getTimeZone("GMT+08:00");
        // dateFormat.setTimeZone(timezone);
        return str;
    }

    public static Date getDate(String date, SimpleDateFormat dateFormat) throws ParseException {
        return dateFormat.parse(date);
    }

    public static String getLongDate(Date date) {
        String str = dateFormatLong.format(date);
        return str;
    }

    public static String getyyyyMMddDateString(Date date) throws ParseException {
        String str = dateFormat2.format(date);
        return str;
    }

    private static String[] getDateRange(String startDate, String endDate) {
        String[] dateStrings = new String[]{startDate, endDate};
        Loger.d("startDate-" + startDate);
        Loger.d("endDate  -" + endDate);
        return dateStrings;
    }

    public static String[] getDateRangeMonth(int start, int len) {
        // 获取（当前-recentMonth）月的第一天
        boolean now = false;
        if (start + len == 1) {
            Loger.d("统计到目前");
            now = true;
        }
        Calendar calendar = Calendar.getInstance(Locale.CHINESE);
        calendar.add(Calendar.MONTH, start);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        String firstday = dateFormat.format(calendar.getTime());
        // 获取当月的最后一天
        calendar = Calendar.getInstance(Locale.CHINESE);
        if (now) {
            calendar.setTime(new Date());
        } else {
            calendar.add(Calendar.MONTH, start + len);
            calendar.set(Calendar.DAY_OF_MONTH, 0);
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
        }
        String lastday = dateFormat.format(calendar.getTime());
        return getDateRange(firstday, lastday);
    }

    public static String[] getDateRangeWeek(int start, int len) {
        // 获取（当前-recentWeek）周的第一天
        boolean now = false;
        if (start + len == 1) {
            Loger.d("统计到目前");
            now = true;
        }
        Calendar calendar = Calendar.getInstance(Locale.CHINESE);
        calendar.setFirstDayOfWeek(Calendar.SUNDAY);
        calendar.add(Calendar.WEEK_OF_YEAR, start);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        String firstday = dateFormat.format(calendar.getTime());
        Loger.d("firstday:" + firstday + "/" + start);
        // 获取当周的最后一天
        calendar = Calendar.getInstance(Locale.CHINESE);
        if (now) {
            calendar.setTime(new Date());
        } else {
            calendar.setFirstDayOfWeek(Calendar.SUNDAY);
            calendar.add(Calendar.WEEK_OF_YEAR, start + len);
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
        }
        String lastday = dateFormat.format(calendar.getTime());
        Loger.d("lastday:" + lastday + "/" + (start + len));
        return getDateRange(firstday, lastday);
    }

    public static String[] getDateRangeDay(int recentDay) {
        if (recentDay < 0) {
            recentDay = 0;
            Loger.d("0<recentDay<31 should ");
        }
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.add(Calendar.DAY_OF_MONTH, 0 - recentDay);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        String firstday = dateFormat.format(calendar.getTime());

        calendar = Calendar.getInstance(Locale.getDefault());
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        String lastday = dateFormat.format(calendar.getTime());
        Loger.d("getDateRangeDay1:" + firstday);
        Loger.d("getDateRangeDay2:" + lastday);
        return getDateRange(firstday, lastday);
    }

    public static String[] getDateRangeDayStr(String[] strs) {
        String[] strings = new String[2];
        try {
            strings[0] = getShortDate(strs[0]);
            strings[1] = getShortDate(strs[1]);
            return strings;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return strs;
    }


    public static String[] getDateRangeDay(Calendar calendarStart, Calendar calendarEnd) {
        calendarStart.set(Calendar.HOUR_OF_DAY, 0);
        calendarStart.set(Calendar.MINUTE, 0);
        calendarStart.set(Calendar.SECOND, 0);

        String firstday = dateFormat.format(calendarStart.getTime());
        String lastday = dateFormat.format(calendarEnd.getTime());
        return getDateRange(firstday, lastday);
    }

    public static String[] getDateRangeDay(int start, int len) {
        Loger.d("统计到目前" + start + "" + len);
        boolean now = false;
        if (start + len == 0) {
            Loger.d("统计到目前");
//XXX			now = true;
        }
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.add(Calendar.DAY_OF_MONTH, 0 + start);
        calendarStart.set(Calendar.HOUR_OF_DAY, 0);
        calendarStart.set(Calendar.MINUTE, 0);
        calendarStart.set(Calendar.SECOND, 0);
        Calendar calendarEnd = Calendar.getInstance();
        if (now) {
            calendarEnd.setTime(new Date());
        } else {
            calendarEnd = Calendar.getInstance();
            calendarEnd.add(Calendar.DAY_OF_MONTH, 0 + start + len);
            calendarEnd.set(Calendar.HOUR_OF_DAY, 23);
            calendarEnd.set(Calendar.MINUTE, 59);
            calendarEnd.set(Calendar.SECOND, 59);
        }
        return getDateRangeDay(calendarStart, calendarEnd);
    }

    /**
     * return yyyyMMddHHmmss format current date
     *
     * @return
     * @Title: getCurrentTime
     * @Description: TODO
     * @return: String
     */
    public static String getCurrentTime() {
        String transTime = getDate(dateFormat);
        return transTime;
    }

    /**
     * 获取本周一日期
     *
     * @return
     */
    public static String getWeekMonday() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        //  System.out.println(cal.getTime());
        String time = dateFormatShort.format(cal.getTime());
        System.out.println(time);
        return time;
    }

    /**
     * 获取本周日日期
     *
     * @return
     */
    public static String getWeekSunday() {
        Calendar cal = Calendar.getInstance();
        //  System.out.println(cal.getTime());
        //这种输出的是上个星期周日的日期，因为老外那边把周日当成第一天
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        //  System.out.println(cal.getTime());

        //增加一个星期，才是我们中国人理解的本周日的日期
        cal.add(Calendar.WEEK_OF_YEAR, 1);
        //  System.out.println(cal.getTime()); //本周日
        String time = dateFormatShort.format(cal.getTime());
        System.out.println(time);
        return time;
    }

    //获取当前月第一天：
    public static String getMonthFirstDay() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
        String first = dateFormatShort.format(c.getTime());
        System.out.println("===============first:" + first);
        return first;
    }

    //获取当前月最后一天
    public static String getMonthEndDay() {
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        String last = dateFormatShort.format(ca.getTime());
        System.out.println("===============last:" + last);
        return last;
    }

    public static String getYear() {
        return dataYear.format(new Date());
    }

    //获取当前周几 0 是周日
    public static int getWeekDay(Date dt) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return w;
    }

    @SuppressLint("SimpleDateFormat")
    public static String formatDate(Date date, String pattern) {
        // TODO Auto-generated method stub
        return new SimpleDateFormat(pattern).format(date);
    }

    public static String getSimpleDate() {
        return new SimpleDateFormat(SIMPLE_FORMAT).format(new Date());
    }

}
