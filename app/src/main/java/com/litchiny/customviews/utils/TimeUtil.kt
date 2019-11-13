package com.litchiny.customviews.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * Time 相关转换的工具类
 *
 * @author Litchiny
 */
object TimeUtil {
    /**
     * 重设一天的开始时间
     *
     * @param calendar
     */
    fun resetDayStart(calendar: Calendar): Calendar {
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        return calendar
    }

    fun timeStampToString(timeStamp: Long): String {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        val len = (timeStamp.toString() + "").length
        return sdf.format(if (len > 10) timeStamp else timeStamp * 1000L)
    }
}
