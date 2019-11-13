package com.litchiny.customviews.utils

import android.content.Context
import android.graphics.Paint
import android.text.TextUtils

/**
 * UnitUtil公共类
 *
 * @author Litchiny
 */
object UnitUtil {

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue 单位为sp的值
     * @return 转换后px的值
     */
    fun sp2px(context: Context, spValue: Float): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (spValue * fontScale + 0.5f).toInt()
    }


    fun px2sp(context: Context, pxValue: Float): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (pxValue / fontScale + 0.5f).toInt()
    }

    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变
     *
     * @param pxValue 要转换px的值
     * @return 转换后dp的值
     */
    fun px2dip(context: Context, pxValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @param dpValue 要转换dp的值
     * @return 转换后px的值
     */
    fun dp2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    /**
     * 米转英里
     *
     * @return 英里
     */
    fun meterToMile(meter: Float): Float {
        val mile = meter * 0.0006214
        return (mile * 100).toInt() / 100f
    }

    /**
     * 英里转米
     *
     * @return 米
     */
    fun mileToMeter(mile: Float): Int {
        val meter = mile * 1609.344
        return meter.toInt()
    }

    /**
     * 米转千米
     *
     * @param meter 米
     * @return 千米
     */
    fun meterToKM(meter: Int): Float {
        return (meter / 1000f * 100).toInt() / 100f
    }


    fun getTextHeight(textPaint: Paint): Float {
        val fm = textPaint.fontMetrics
        return Math.ceil((fm.descent - fm.ascent).toDouble()).toFloat() - 2
    }

    fun getTextWidth(textPaint: Paint, text: String): Float {
        var text = text
        text = if (TextUtils.isEmpty(text)) " " else text
        return textPaint.measureText(text)
    }
}
