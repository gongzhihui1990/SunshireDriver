package net.iaf.framework.util;

import android.text.TextUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 时间的工具类
 *
 * @author zgg
 */
public class TimeHelper {

    private static final int DAY = 86400;
    private static final int HOUR = 3600;
    private static final int MINUTE = 60;
    private static String DEFAULT_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

    /**
     * 获取当前日期时间
     *
     * @param format 日期时间格式
     * @return 日期时间字符串
     */
    public static String getCurrentDateStr(String format) {
        return new SimpleDateFormat(format).format(new java.util.Date());
    }

    /**
     * 获取当前日期,默认格式"yyyy-MM-dd";
     *
     * @return 日期字符串
     */
    public static String getCurrentDateStr() {
        return new SimpleDateFormat(DEFAULT_DATE_FORMAT)
                .format(new java.util.Date());
    }

    public static String getTomorrowDateStr() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        return new SimpleDateFormat("MM月dd日")
                .format(calendar.getTime());

    }

    /**
     * 获取当前日期时间,默认格式"yyyy-MM-dd HH:mm:ss";
     *
     * @return 日期时间字符串
     */
    public static String getCurrentTimeStr() {
        return new SimpleDateFormat(DEFAULT_TIME_FORMAT)
                .format(new java.util.Date());
    }

    /**
     * 根据传入的Calendar，格式化成给定的format的日期
     *
     * @return 日期字符串
     */
    public static String getFormattedDate(String format, Calendar calendar) {
        return new SimpleDateFormat(format).format(calendar.getTime());
    }

    /**
     * 根据传入的Date，格式化成给定的format的日期
     *
     * @return 日期字符串
     */
    public static String getFormattedDate(String format, Date date) {
        return new SimpleDateFormat(format).format(date);
    }

    /**
     * 获取当前是第几季度
     */
    public static int getCurrentQuarter() {
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1;
        if (month % 3 == 0) {
            return month / 3;
        } else {
            return month / 3 + 1;
        }
    }

    /**
     * 获得输入的calendar的季度
     */
    public static int getQuarter(Calendar calendar) {
        int month = calendar.get(Calendar.MONTH) + 1;
        if (month % 3 == 0) {
            return month / 3;
        } else {
            return month / 3 + 1;
        }
    }

    /**
     * 给定的日期是否晚于当前日期
     */
    public static boolean isDateLaterThanCurrent(Calendar calendar) {
        Calendar tomorrow = Calendar.getInstance();
        tomorrow.add(Calendar.DATE, 1);
        tomorrow.set(Calendar.HOUR_OF_DAY, 0);
        tomorrow.set(Calendar.MINUTE, 0);
        tomorrow.set(Calendar.SECOND, 0);
        return !calendar.before(tomorrow);
    }

    /**
     * 给定的年月是否晚于当前
     */
    public static boolean isMonthLaterThanCurrent(int year, int month) {
        checkYearAndMonth(year, month);

        Calendar current = Calendar.getInstance();
        final int currentYear = current.get(Calendar.YEAR);
        if (year < currentYear) {
            return false;
        } else if (year > currentYear) {
            return true;
        } else {
            int currentMonth = current.get(Calendar.MONTH) + 1;
            if (month > currentMonth) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * 给定的季度是否晚于当前
     */
    public static boolean isQuarterLaterThanCurrent(int year, int quarter) {
        checkYearAndQuarter(year, quarter);

        Calendar current = Calendar.getInstance();
        final int currentYear = current.get(Calendar.YEAR);
        if (year < currentYear) {
            return false;
        } else if (year > currentYear) {
            return true;
        } else {
            int currentQuarter = getCurrentQuarter();
            if (quarter > currentQuarter) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * 获取指定的月份的第一天的日期
     */
    public static Date getFirstDateOfMonth(int year, int month) {
        checkYearAndMonth(year, month);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DATE, 1);
        return calendar.getTime();
    }

    /**
     * 获取指定的月份的最后一天的日期
     */
    public static Date getLastDateOfMonth(int year, int month) {
        checkYearAndMonth(year, month);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DATE, 1);
        calendar.add(Calendar.DATE, -1);
        return calendar.getTime();
    }

    /**
     * 获取指定的季度的第一天的日期
     */
    public static Date getFirstDateOfQuarter(int year, int quarter) {
        checkYearAndQuarter(year, quarter);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, 3 * quarter - 3);
        calendar.set(Calendar.DATE, 1);
        return calendar.getTime();
    }

    /**
     * 获取指定的季度的最后一天的日期
     */
    public static Date getLastDateOfQuarter(int year, int quarter) {
        checkYearAndQuarter(year, quarter);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, 3 * quarter);
        calendar.set(Calendar.DATE, 1);
        calendar.add(Calendar.DATE, -1);
        return calendar.getTime();
    }

    /**
     * 获取指定的年的最后一天的日期
     */
    public static Date getFirstDateOfYear(int year) {
        checkYear(year);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.DATE, 1);
        return calendar.getTime();
    }

    /**
     * 获取昨天的日历
     */
    public static Calendar getYesterday() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        return calendar;
    }

    /**
     * 检查年跟月的正确性
     *
     * @param year
     * @param month
     */
    private static void checkYearAndMonth(int year, int month) {
        checkYear(year);
        checkMonth(month);
    }

    /**
     * 检查年跟季度的正确性
     *
     * @param year
     * @param quarter
     */
    private static void checkYearAndQuarter(int year, int quarter) {
        checkYear(year);
        checkQuarter(quarter);
    }

    /**
     * 检查年的正确性
     *
     * @param year
     */
    private static void checkYear(int year) {
        if (year <= 0) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * 检查月的正确性
     *
     * @param month
     */
    private static void checkMonth(int month) {
        if (!(0 < month && month <= 12)) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * 检查季度的正确性
     *
     * @param quarter
     */
    private static void checkQuarter(int quarter) {
        if (!(0 < quarter && quarter <= 4)) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * 根据截止时间秒转换格式
     *
     * @param strdeadline
     * @return
     */
    public static String getDeadline(String strdeadline) {
        int lasttemp = Integer.valueOf(strdeadline);
        int days = 0;
        int hours = 0;
        int mins = 0;
        String result = "";
        if (lasttemp >= DAY) {
            days = lasttemp / DAY;
            lasttemp = lasttemp % DAY;
            result = days + "天";

            hours = lasttemp / HOUR;
            lasttemp = lasttemp % HOUR;
            result = result + hours + "时";

            mins = lasttemp / MINUTE;
            lasttemp = lasttemp % MINUTE;
            result = result + mins + "分";

        } else if (lasttemp > HOUR) {
            hours = lasttemp / HOUR;
            lasttemp = lasttemp % HOUR;
            result = hours + "时";

            mins = lasttemp / MINUTE;
            lasttemp = lasttemp % MINUTE;
            result = result + mins + "分";

            result = result + lasttemp + "秒";

        } else if (lasttemp > MINUTE) {
            mins = lasttemp / MINUTE;
            lasttemp = lasttemp % MINUTE;
            result = mins + "分";

            result = result + lasttemp + "秒";
        } else {
            result = result + lasttemp + "秒";
        }

        return result;
    }

    /**
     * get hour and minute
     *
     * @param strdeadline
     * @return
     */
    public static String getHourAndMinute(String strdeadline) {
        String fmt = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(fmt);
        Calendar calendar = Calendar.getInstance();
        try {
            java.util.Date date = sdf.parse(strdeadline);
            calendar.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return String.format("%tR", calendar);
    }

    /**
     * 微博分享模块 时间格式转换
     *
     * @param systemTime  系统时间
     * @param createdTime 上传时间
     * @return
     * @Title: switchTime
     * @Description: TODO
     */
    public static String switchTime(String systemTime, String createdTime) {

        long between = (Long.valueOf(systemTime)
                - Long.valueOf(createdTime)) / 1000;
        long year = between / (24 * 3600 * 30 * 12);
        if (year != 0) {
            if (year == 1) {
                return "1年前";
            } else {
                return year + "年前";
            }
        } else {
            long month = between / (24 * 3600 * 30);
            if (month != 0) {
                if (month == 1) {
                    return "1个月前";
                } else {
                    return month + "个月前";
                }
            } else {
                long day = between / (24 * 3600);
                if (day != 0) {
                    if (day == 1) {
                        return "1天前";
                    } else if (day < 7) {
                        return day + "天前";
                    } else if (day < 14) {
                        return "1周前";
                    } else {
                        if (day / 7 == 4) {
                            return "1个月前";
                        } else {
                            return day / 7 + "周前";
                        }
                    }
                } else {
                    long hour = between % (24 * 3600) / 3600;
                    if (hour != 0) {
                        if (hour == 1) {
                            return "1小时前";
                        } else {
                            return hour + "小时前";
                        }
                    } else {
                        long minute = between % 3600 / 60;
                        if (minute != 0) {
                            if (minute == 1) {
                                return "1分钟前";
                            } else {
                                return minute + "分钟前";
                            }
                        } else {
                            return "刚刚";
                        }
                    }
                }
            }
        }
    }

    /**
     * 获取时间
     *
     * @param createtime
     * @return
     */
    public static String getTime(String createtime) {
        try {
            int year = Integer.parseInt(createtime.substring(0, 4));
            int month = Integer.parseInt(createtime.substring(4, 6)) - 1;
            int day = Integer.parseInt(createtime.substring(6, 8));
            int hour = Integer.parseInt(createtime.substring(8, 10));
            int minute = Integer.parseInt(createtime.substring(10, 12));
            int second = Integer.parseInt(createtime.substring(12, 14));
            GregorianCalendar gc = new GregorianCalendar(year, month, day,
                    hour, minute, second);

            Date date = gc.getTime();
            long pass = date.getTime();
            long now = System.currentTimeMillis();
            GregorianCalendar gcNow = new GregorianCalendar();
            // System.out.println("now-pass:" + ((now-pass)/1000/60/60));

            if ((now - pass) / 1000 < 60) {
                if (now - pass <= 0) {
                    return "刚刚";
                } else {
                    return ((now - pass) / 1000) + "秒以前";
                }
            } else if ((now - pass) / 1000 / 60 < 60) {
                return ((now - pass) / 1000 / 60) + "分钟以前";
            } else if ((now - pass) / 1000 / 60 / 60 / 24 == 0) {
                if (gcNow.get(Calendar.DAY_OF_MONTH) == day) {
                    return "今天 " + createtime.substring(8, 10) + ":"
                            + createtime.substring(10, 12);
                } else {
                    return createtime.substring(4, 6) + "-"
                            + createtime.substring(6, 8) + " "
                            + createtime.substring(8, 10) + ":"
                            + createtime.substring(10, 12);
                }
            } else {
                return createtime.substring(4, 6) + "-"
                        + createtime.substring(6, 8) + " "
                        + createtime.substring(8, 10) + ":"
                        + createtime.substring(10, 12);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 返回两个日期之间的间隔天数
     *
     * @param dateFrom yyyy/MM/dd格式的日期
     * @param now      当前日期
     * @return 间隔的天数
     */
    public static int betweenTwoDays(String dateFrom, Date now) {

        try {
            if (TextUtils.isEmpty(dateFrom)) {
                return 0;
            }

            Calendar fromCalendar = Calendar.getInstance();
            SimpleDateFormat fmtshort = new SimpleDateFormat("yyyy-MM-dd");
            fromCalendar.setTime(fmtshort.parse(dateFrom));
            fromCalendar.set(Calendar.HOUR, 0);
            fromCalendar.set(Calendar.MINUTE, 0);
            fromCalendar.set(Calendar.SECOND, 0);
            fromCalendar.set(Calendar.MILLISECOND, 0);

            Calendar fromCalendar1 = Calendar.getInstance();
            fromCalendar1.setTime(now);
            fromCalendar.set(Calendar.HOUR, 0);
            fromCalendar.set(Calendar.MINUTE, 0);
            fromCalendar.set(Calendar.SECOND, 0);
            fromCalendar.set(Calendar.MILLISECOND, 0);

            return (int) ((fromCalendar1.getTime().getTime() - fromCalendar
                    .getTime().getTime()) / (24 * 60 * 60 * 1000)) + 1;
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 返回两个日期之间的间隔秒数
     *
     * @param dateFrom yyyyMMddHHmmss格式的日期
     * @param now      当前日期
     * @return 间隔秒数
     */
    public static int betweenTwoDaySeconds(String dateFrom, Date now) {

        SimpleDateFormat fmt1 = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            if (dateFrom == null || dateFrom.length() == 0)
                return 0;
            Calendar fromCalendar1 = Calendar.getInstance();
            fromCalendar1.setTime(fmt1.parse(dateFrom));


            Calendar fromCalendar = Calendar.getInstance();
            fromCalendar.setTime(fmt1.parse(fmt1.format(now)));


            return (int) (fromCalendar.getTime().getTime() - fromCalendar1
                    .getTime().getTime()) / 1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 把字符串转换为日期
     *
     * @param dateStr 日期字符串
     * @param format  日期格式
     * @return Date
     */

    public static Date strToDate(String dateStr, String format) {
        Date date = null;
        if (dateStr != null && (!dateStr.equals(""))) {
            DateFormat df = new SimpleDateFormat(format);
            try {
                date = df.parse(dateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return date;
    }

    /**
     * 把日期转换为字符串
     *
     * @param date   日期实例
     * @param format 日期格式
     * @return Date
     */

    public static String dateToStr(Date date, String format) {
        return (date == null) ? "" : new SimpleDateFormat(format).format(date);
    }

    /**
     * 将剩余的秒数格式化为XX天XX时XX分XX秒，时间单位秒
     *
     * @param leftSeconds
     * @return
     */
    public static String formatLeftTimes(long leftSeconds) {
        StringBuffer sb = new StringBuffer();
        if (leftSeconds < 0) {
            return "";
        } else {
            if (leftSeconds >= 86400) {
                sb.append(String.valueOf(leftSeconds / 86400))
                        .append("天");
                leftSeconds = leftSeconds % 86400;
                sb.append(String.valueOf(leftSeconds / 3600))
                        .append("时");
                leftSeconds = leftSeconds % 3600;
                sb.append(String.valueOf(leftSeconds / 60))
                        .append("分");
            } else if (leftSeconds >= 3600) {
                sb.append(String.valueOf(leftSeconds / 3600))
                        .append("时");
                leftSeconds = leftSeconds % 3600;
                sb.append(String.valueOf(leftSeconds / 60))
                        .append("分");
                leftSeconds = leftSeconds % 60;
                sb.append(String.valueOf(leftSeconds))
                        .append("秒");
            } else if (leftSeconds >= 60) {
                sb.append(String.valueOf(leftSeconds / 60))
                        .append("分");
                leftSeconds = leftSeconds % 60;
                sb.append(String.valueOf(leftSeconds))
                        .append("秒");
            } else {
                sb.append(String.valueOf(leftSeconds))
                        .append("秒");
            }
        }
        return sb.toString();
    }

    /**
     * 判断两个Calendar对象日期是否相同（年月日是否都相同）
     */
    public static boolean hasSameDate(Calendar c1, Calendar c2) {
        if ((c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR))
                && (c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH))
                && (c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH))) {
            return true;
        }
        return false;
    }

    /**
     * 判断两个Calendar对象时间是否相同（年月日时分是否都相同）
     */
    public static boolean hasSameMinute(Calendar c1, Calendar c2) {
        if ((c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR))
                && (c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH))
                && (c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH))
                && (c1.get(Calendar.HOUR_OF_DAY) == c2.get(Calendar.HOUR_OF_DAY))
                && (c1.get(Calendar.MINUTE) == c2.get(Calendar.MINUTE))) {
            return true;
        }
        return false;
    }

    public static String after30MinToNowDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) + 30);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = df.format(calendar.getTime());
        return dateStr;
    }

    public static String after30MinToNowTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) + 30);
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        SimpleDateFormat df1 = new SimpleDateFormat("mm");
        SimpleDateFormat df2 = new SimpleDateFormat("HH");
        String dateStr = "";
        String hourStr = "";
        String minuteStr = "";
        int hourInt = Integer.valueOf(df2.format(calendar.getTime()));
        int minuteInt = Integer.valueOf(df1.format(calendar.getTime()));
        if (minuteInt % 5 == 0) {
            dateStr = df.format(calendar.getTime());
        } else {
            //hour
            if (hourInt < 10) {
                hourStr = "0" + hourInt;
            } else {
                hourStr = hourInt + "";
            }

            //minute
            int newminute = minuteInt - minuteInt % 5 + 5;
            if (newminute < 10) {
                minuteStr = "0" + newminute;
            } else if (newminute < 60) {
                minuteStr = "" + newminute;
            } else {
                minuteStr = "00";
                hourInt = hourInt + 1;
                if (hourInt < 10) {
                    hourStr = "0" + hourInt;
                } else {
                    hourStr = hourInt + "";
                }
            }
            dateStr = hourStr + ":" + minuteStr;
        }
        return dateStr;
    }

    /**
     * 是否超出发车时间
     */
    public static boolean isBeforeNow(String time) {
        try {
            Date startTime = new SimpleDateFormat("HH:mm").parse(time);
            Date now = new SimpleDateFormat("HH:mm").parse(TimeHelper
                    .getFormattedDate("HH:mm", new Date()));
            if (startTime.before(now)) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }
}
