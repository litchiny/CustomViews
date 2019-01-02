package com.litchiny.customviews.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View

/**
 * 电池view
 *
 * @author Litchiny
 */
class BatteryView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {
    var completeProgress: Int = 0
    private var rectPaint: Paint? = null
    private var textPaint: Paint? = null
    private var path: Path? = null
    private var rectX = 100
    private var rectY = 200
    private var topRectX: Int = 0
    private var topRectY: Int = 0
    private var topRectStartX: Int = 0
    private var baseInside: Int = 0
    private var allStartX: Int = 0
    private var allStartY: Int = 0
    private var textSize: Int = 0
    private var distanceW: Int = 0
    private var distanceH: Int = 0
    private var lowBattery: Int = 0

    init {
        initPaint()
    }

    private fun initPaint() {
        //边框画笔
        rectPaint = Paint()
        rectPaint?.isAntiAlias = true
        rectPaint?.style = Paint.Style.FILL
        //文字画笔
        textPaint = Paint()
        textPaint?.isAntiAlias = true
        textPaint?.color = Color.DKGRAY
        textPaint?.style = Paint.Style.FILL
        textPaint?.textAlign = Paint.Align.LEFT
        path = Path()
    }

    fun setTextColor(color: Int) {
        textPaint?.color = color
        initPaint()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        rectPaint?.style = Paint.Style.FILL
        resetPaintColor(rectPaint)
        canvas.drawRect(Rect(allStartX + topRectStartX, allStartY, allStartX + topRectX + topRectStartX, topRectY + allStartY), rectPaint!!)   //画实心
        rectPaint?.style = Paint.Style.STROKE
        canvas.drawPath(path!!, rectPaint!!)
        resetPaintColor(textPaint)
        if (completeProgress <= lowBattery) {
            textPaint?.textSize = (rectX * 8 / 5).toFloat()
            canvas.drawText("!", (allStartX + rectX / 2 - baseInside).toFloat(), (rectY - baseInside + allStartY).toFloat(), textPaint!!)
        } else {
            rectPaint?.style = Paint.Style.FILL
            rectPaint?.color = Color.GREEN
            val num = (rectY - 2 * baseInside) * (100 - completeProgress) / 100 + allStartY + topRectY + baseInside
            canvas.drawRect(Rect(allStartX + baseInside, num, allStartX + rectX - baseInside, rectY + topRectY - baseInside + allStartY), rectPaint!!)
        }
        textPaint?.textSize = textSize.toFloat()
        canvas.drawText(completeProgress.toString() + "%", rectX * 1.4f + allStartX, (distanceH / 2 + baseInside * 2).toFloat(), textPaint!!)
    }

    private fun resetPaintColor(paint: Paint?) {
        paint?.color = if (completeProgress <= lowBattery) Color.RED else Color.GRAY
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val modeWidth = View.MeasureSpec.getMode(widthMeasureSpec)
        val modeHeight = View.MeasureSpec.getMode(heightMeasureSpec)
        val sizeWidth = View.MeasureSpec.getSize(widthMeasureSpec)
        val sizeHeight = View.MeasureSpec.getSize(heightMeasureSpec)
        var resultWidth = sizeWidth
        var resultHeight = sizeHeight

        // 考虑内边距对尺寸的影响
        resultWidth += paddingLeft + paddingRight
        resultHeight += paddingTop + paddingBottom
        // 考虑父容器对尺寸的影响
        resultWidth = measureSize(modeWidth, sizeWidth, resultWidth)
        distanceW = resultWidth
        resultHeight = measureSize(modeHeight, sizeHeight, resultHeight)
        distanceH = resultHeight
        setMeasuredDimension(resultWidth, resultHeight)
        if (distanceW > distanceH) {
            rectY = distanceH / 2
            rectX = rectY / 2
        } else {
            rectX = distanceW / 4
            rectY = rectX * 2
        }
        resetValue()
        //        Log.i(TAG, "onMeasure: rectX: " + rectX + ",rectY: " + rectY);
    }

    private fun measureSize(mode: Int, sizeExpect: Int, sizeActual: Int): Int {
        var realSize = sizeExpect
        if (mode != View.MeasureSpec.EXACTLY) {
            realSize = sizeActual
            if (mode == View.MeasureSpec.AT_MOST)
                realSize = Math.min(realSize, sizeExpect)
        }
        return realSize
    }

    private fun resetValue() {
        val mStrokeWidth = rectX / 10f
        topRectY = rectX / 5
        topRectX = rectX / 2
        val smallSpace = rectX / 7                                                                     //折角的基本单位
        allStartX = distanceW / 2 - rectX - smallSpace
        allStartY = (distanceH - topRectY - rectY) / 2
        topRectStartX = (rectX - topRectX) / 2
        baseInside = rectX / 5
        textSize = rectX * 9 / 10
        path?.reset()
        path?.moveTo((allStartX + smallSpace).toFloat(), (topRectY + allStartY).toFloat())
        path?.lineTo((allStartX + rectX - smallSpace).toFloat(), (topRectY + allStartY).toFloat())
        path?.lineTo((allStartX + rectX).toFloat(), (smallSpace + topRectY + allStartY).toFloat())
        path?.lineTo((allStartX + rectX).toFloat(), (rectY - smallSpace + topRectY + allStartY).toFloat())
        path?.lineTo((allStartX + rectX - smallSpace).toFloat(), (rectY + topRectY + allStartY).toFloat())
        path?.lineTo((allStartX + smallSpace).toFloat(), (rectY + topRectY + allStartY).toFloat())
        path?.lineTo(allStartX.toFloat(), (rectY - smallSpace + topRectY + allStartY).toFloat())
        path?.lineTo(allStartX.toFloat(), (smallSpace + topRectY + allStartY).toFloat())
        path?.close()
        rectPaint?.strokeWidth = mStrokeWidth
    }

    fun setData(completeProgress: Int, lowBattery: Int) {
        var completeProgress = completeProgress
        var lowBattery = lowBattery
        completeProgress = if (completeProgress < 0) 0 else if (completeProgress > 100) 100 else completeProgress
        lowBattery = if (lowBattery < 0) 0 else lowBattery
        this.completeProgress = completeProgress
        this.lowBattery = lowBattery
        invalidate()
    }

    companion object {
        private val TAG = "BatteryView"
    }
}
