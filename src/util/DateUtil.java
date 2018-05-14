package util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期处理工具类
 *
 * @author yinyiyun
 * @date 2018/5/2 10:32
 */
public class DateUtil {

    private static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final Date BASE_DATE = getBaseDate();

    private static Date getBaseDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(1970, 0, 1, 8, 0, 0);
        return calendar.getTime();
    }

    public static Date getDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day, 0, 0, 0);
        return calendar.getTime();
    }

    /**
     * 根据字符串获取日期
     *
     * @param date   日期
     * @param format 格式 : yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static Date getDateByString(String date, String format) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            return simpleDateFormat.parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 根据字符串获取日期
     *
     * @param date 日期
     * @return
     */
    public static Date getDateByString(String date) {
        return getDateByString(date, DEFAULT_FORMAT);
    }

    /**
     * 将日期转换为固定格式字符串
     *
     * @param date   日期
     * @param format 格式 : yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String transDateToString(Date date, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }

    /**
     * 将日期转换为默认格式字符串
     *
     * @param date 日期
     * @return
     */
    public static String transDateToString(Date date) {
        return transDateToString(date, DEFAULT_FORMAT);
    }

    /**
     * 将时间戳转换为固定格式字符串
     *
     * @param time   时间戳
     * @param format 格式 : yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String transDateToString(Long time, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(time);
    }

    /**
     * 将日期转换为默认格式字符串
     *
     * @param time 时间戳
     * @return
     */
    public static String transDateToString(Long time) {
        return transDateToString(time, DEFAULT_FORMAT);
    }

    /**
     * 将日期转换为时间戳类型
     *
     * @param date 日期
     * @return
     */
    public static Timestamp transDateToStamp(Date date, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Timestamp timestamp = Timestamp.valueOf(simpleDateFormat.format(date));
        return timestamp;
    }

    /**
     * 将日期转换为时间戳类型
     *
     * @param date 日期
     * @return
     */
    public static Timestamp transDateToStamp(Date date) {
        return transDateToStamp(date, DEFAULT_FORMAT);
    }

}
