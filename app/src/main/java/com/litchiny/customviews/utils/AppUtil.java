package com.litchiny.customviews.utils;

import android.graphics.Paint;

/**
 * author: LL .
 * date:   On 2019/1/2
 * caption:
 */


public class AppUtil {
    public static Paint getAvailablePaint(int color, int strokeWidth, Paint.Style style) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FAKE_BOLD_TEXT_FLAG);
        paint.setAntiAlias(true);
        paint.setColor(color);
        paint.setStyle(style);
        paint.setStrokeWidth(strokeWidth);

        paint.setDither(true);//设置图像抖动处理
        paint.setStrokeJoin(Paint.Join.ROUND);//画笔线等连接处的轮廓样式
        paint.setSubpixelText(true);
        return paint;
    }
}
