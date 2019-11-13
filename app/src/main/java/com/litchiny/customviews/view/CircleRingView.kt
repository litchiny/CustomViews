package com.litchiny.customviews.view

import android.animation.ArgbEvaluator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Shader
import android.util.AttributeSet
import android.util.Log
import android.view.View


import com.litchiny.customviews.R
import com.litchiny.customviews.model.CircleRingMode
import com.litchiny.customviews.utils.AppUtil
import com.litchiny.customviews.utils.UnitUtil

import java.util.ArrayList


/**
 * 一个支持圆环不同类型展示的view
 *
 * @author Litchiny
 */
class CircleRingView @JvmOverloads constructor(private val mContext: Context, attrs: AttributeSet? = null) : View(mContext, attrs) {
    private val mColors: IntArray
    private var circlePaint: Paint? = null                                                                      //表盘外轮廓画笔
    private var linePaint: Paint? = null                                                                       // 表盘内短线画笔
    private var mWidth: Int = 0
    private var mHeight: Int = 0
    private var startPercent = 0f
    private var initStartPercent: Float = 0.toFloat()
    //圆环的画笔
    private var cyclePaint: Paint? = null
    //圆的直径
    private var mRadius = 300f
    //圆的粗细
    private var mStrokeWidth = 80f
    private val ringModes = ArrayList<CircleRingMode>()
    private var startY = 0
    private val argbEvaluator = ArgbEvaluator()//颜色渐变插值器
    private var radius: Int = 0

    init {
        mColors = intArrayOf(mContext.resources.getColor(R.color.colorSleepGradient2), mContext.resources.getColor(R.color.colorSleepGradient3), mContext.resources.getColor(R.color.colorSleepGradient), mContext.resources.getColor(R.color.colorSleepGradient4))
        initPaint()
    }

    /**
     * 创建所有的paint
     */
    private fun initPaint() {
        val strokeWidth = UnitUtil.dp2px(mContext, 2.5f)
        circlePaint = AppUtil.getAvailablePaint(Color.BLUE, strokeWidth, Paint.Style.STROKE)
        linePaint = AppUtil.getAvailablePaint(mContext.resources.getColor(R.color.colorSleepGradient), strokeWidth, Paint.Style.FILL)
        linePaint!!.textSize = UnitUtil.dp2px(context, 12f).toFloat()
        mStrokeWidth = UnitUtil.dp2px(mContext, 24f).toFloat()
        cyclePaint = AppUtil.getAvailablePaint(Color.BLUE, mStrokeWidth.toInt(), Paint.Style.STROKE)
        startY = UnitUtil.dp2px(mContext, 10f)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        circlePaint!!.shader = LinearGradient(3f, 3f, (mWidth - 3).toFloat(), (mHeight - 3).toFloat(), mColors, null, Shader.TileMode.CLAMP)
        canvas.drawCircle((mWidth / 2).toFloat(), (mHeight / 2).toFloat(), radius.toFloat(), circlePaint!!)
        mRadius = (radius * 2 - startY * 6).toFloat()
        canvas.translate(mWidth / 2 - mRadius / 2, mHeight / 2 - mRadius / 2)
        if (ringModes.size == 0)
            return
        //画圆环
        drawCycle(canvas)
        drawSquare(canvas)
    }

    //画圆环
    private fun drawCycle(canvas: Canvas) {
        var sweepPercent = 0f
        var mode: CircleRingMode
        startPercent = initStartPercent
        for (i in ringModes.indices) {
            mode = ringModes[i]
            cyclePaint!!.color = mode.colorValue
            startPercent += sweepPercent - 0.5f
            if (i < 2) {
                sweepPercent = (mode.progress + 0.5f) * 360 / 100f
                startPercent -= 1f
            } else if (i == ringModes.size - 1) {
                sweepPercent = 360f + initStartPercent - startPercent - 0.5f
            } else {
                sweepPercent = mode.progress * 360 / 100f
            }
            canvas.drawArc(RectF(0f, 0f, mRadius, mRadius), startPercent, sweepPercent, false, cyclePaint!!)
        }
    }

    private fun drawSquare(canvas: Canvas) {
        val base = Math.min(mWidth, mHeight) / 25
        var startTop = mHeight / 2 - base * 3
        val startLeft = mWidth / 2 + base
        for (mode in ringModes) {
            linePaint!!.color = mode.colorValue
            canvas.drawRect(Rect(startLeft, startTop, startLeft + base, startTop + base), linePaint!!)
            canvas.drawText(mode.progress.toString() + "%", (startLeft + base + 10).toFloat(), (startTop + base).toFloat(), linePaint!!)
            startTop += base * 2
        }
    }

    /**
     * 获取宽和高
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mWidth = View.getDefaultSize(suggestedMinimumWidth, widthMeasureSpec)
        mHeight = View.getDefaultSize(suggestedMinimumWidth, heightMeasureSpec)
        radius = Math.min(mWidth / 2, mHeight / 2) - UnitUtil.dp2px(mContext, 24f)
        Log.d(TAG, "onMeasure: mWidth: $mWidth,mHeight: $mHeight")
    }


    fun setData(modeList: List<CircleRingMode>) {
        this.setData(modeList, 0f)
    }

    fun setData(modeList: List<CircleRingMode>, hour: Float) {
        if (modeList.size == 0)
            return
        if (this.ringModes.size > 0)
            this.ringModes.clear()
        this.ringModes.addAll(modeList)
        this.initStartPercent = 360 * hour / 24f - 90
        this.startPercent = this.initStartPercent
        invalidate()
    }

    companion object {
        private val TAG = "SleepRingView"
    }
}

