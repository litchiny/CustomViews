package com.litchiny.customviews.utils

import android.graphics.Paint

/**
 * APP公共类
 *
 * @author Litchiny
 */


object AppUtil {
    fun getAvailablePaint(color: Int, strokeWidth: Int, style: Paint.Style): Paint {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FAKE_BOLD_TEXT_FLAG)
        paint.isAntiAlias = true
        paint.color = color
        paint.style = style
        paint.strokeWidth = strokeWidth.toFloat()

        paint.isDither = true//设置图像抖动处理
        paint.strokeJoin = Paint.Join.ROUND//画笔线等连接处的轮廓样式
        paint.isSubpixelText = true
        return paint
    }
}
