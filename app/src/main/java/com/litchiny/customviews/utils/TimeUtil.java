package com.litchiny.customviews.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Time 相关转换的工具类
 *
 * @author Litchiny
 */
public class TimeUtil {
    /**
     * 重设一天的开始时间
     *
     * @param calendar
     */
    public static Calendar resetDayStart(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar;
    }

    public static String timeStampToString(long timeStamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        int len = (timeStamp + "").length();
        return sdf.format(len > 10 ? timeStamp : (timeStamp * 1000L));
    }
}
