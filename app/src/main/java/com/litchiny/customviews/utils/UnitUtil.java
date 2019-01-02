package com.litchiny.customviews.utils;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;

/**
 * UnitUtil公共类
 *
 * @author Litchiny
 */
public class UnitUtil {

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue 单位为sp的值
     * @return 转换后px的值
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }


    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变
     *
     * @param pxValue 要转换px的值
     * @return 转换后dp的值
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @param dpValue 要转换dp的值
     * @return 转换后px的值
     */
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 米转英里
     *
     * @return 英里
     */
    public static float meterToMile(float meter) {
        double mile = meter * 0.0006214;
        return ((int) (mile * 100)) / 100f;
    }

    /**
     * 英里转米
     *
     * @return 米
     */
    public static int mileToMeter(float mile) {
        double meter = mile * 1609.344;
        return (int) meter;
    }

    /**
     * 米转千米
     *
     * @param meter 米
     * @return 千米
     */
    public static float meterToKM(int meter) {
        return (int) (meter / 1000f * 100) / 100f;
    }


    public static float getTextHeight(Paint textPaint) {
        Paint.FontMetrics fm = textPaint.getFontMetrics();
        return (float) Math.ceil(fm.descent - fm.ascent) - 2;
    }

    public static float getTextWidth(Paint textPaint, String text) {
        text = TextUtils.isEmpty(text) ? " " : text;
        return textPaint.measureText(text);
    }
}
