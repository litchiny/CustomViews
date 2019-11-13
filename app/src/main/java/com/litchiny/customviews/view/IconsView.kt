package com.litchiny.customviews.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.litchiny.customviews.R
import com.litchiny.customviews.utils.AppUtil
import com.litchiny.customviews.utils.UnitUtil

/**
 * Icons的集合
 *
 * @author Litchiny
 */

class IconsView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {
    private var mPaint: Paint? = null
    private var mPath: Path? = null
    private var startRect: Int = 0
    private var baseWidth: Int = 0

    init {
        initPaint()
    }

    private fun initPaint() {
        mPaint = AppUtil.getAvailablePaint(ContextCompat.getColor(context, R.color.colorPrimaryDark), UnitUtil.dp2px(context, 2f), Paint.Style.STROKE)
        mPath = Path()
        startRect = 100
        baseWidth = 50
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawCar(canvas)
        drawShopCart(canvas)
    }

    private fun drawCar(canvas: Canvas) {
        mPaint!!.style = Paint.Style.STROKE
        val startPath = startRect + baseWidth
        canvas.drawRoundRect(RectF(startRect.toFloat(), startRect.toFloat(), startPath.toFloat(), startPath.toFloat()), 5f, 5f, mPaint!!)
        mPath!!.reset()
        mPath!!.moveTo(startPath.toFloat(), (startRect + baseWidth / 3).toFloat())
        mPath!!.lineTo((startPath + baseWidth / 3).toFloat(), (startRect + baseWidth / 3).toFloat())
        mPath!!.lineTo((startPath + baseWidth / 2).toFloat(), (startRect + baseWidth / 5 * 4).toFloat())
        mPath!!.lineTo((startPath + baseWidth / 2).toFloat(), (startRect + baseWidth).toFloat())
        mPath!!.lineTo(startPath.toFloat(), (startRect + baseWidth).toFloat())
        mPath!!.close()
        canvas.drawPath(mPath!!, mPaint!!)
        val raidus = baseWidth / 5
        mPaint!!.color = Color.WHITE
        mPaint!!.style = Paint.Style.FILL
        canvas.drawCircle((startPath + raidus).toFloat(), (startRect + baseWidth).toFloat(), raidus.toFloat(), mPaint!!)
        canvas.drawCircle(startRect + raidus * 1.5f, (startRect + baseWidth).toFloat(), raidus.toFloat(), mPaint!!)
        mPaint!!.style = Paint.Style.STROKE
        mPaint!!.color = ContextCompat.getColor(context, R.color.colorPrimaryDark)
        canvas.drawCircle((startPath + raidus).toFloat(), (startRect + baseWidth).toFloat(), raidus.toFloat(), mPaint!!)
        canvas.drawCircle(startRect + raidus * 1.5f, (startRect + baseWidth).toFloat(), raidus.toFloat(), mPaint!!)
    }

    private fun drawShopCart(canvas: Canvas) {
        val y = startRect
        val x = startRect + baseWidth * 3
        mPath!!.reset()
        mPath!!.moveTo(x.toFloat(), y.toFloat())
        mPath!!.lineTo((x + baseWidth / 4).toFloat(), y.toFloat())
        mPath!!.lineTo((x + baseWidth / 2).toFloat(), (y + baseWidth).toFloat())
        mPath!!.lineTo((x + baseWidth / 4).toFloat(), (y + baseWidth / 4 * 5).toFloat())
        mPath!!.lineTo((x + baseWidth / 4 * 5).toFloat(), (y + baseWidth / 4 * 5).toFloat())
        canvas.drawPath(mPath!!, mPaint!!)
        mPath!!.reset()
        mPath!!.moveTo((x + baseWidth / 4 + 5).toFloat(), (y + baseWidth / 12 * 5).toFloat())
        mPath!!.lineTo((x + baseWidth * 5 / 4).toFloat(), (y + baseWidth / 12 * 5).toFloat())
        mPath!!.lineTo((x + baseWidth).toFloat(), (y + baseWidth).toFloat())
        mPath!!.lineTo((x + baseWidth / 2).toFloat(), (y + baseWidth).toFloat())
        mPath!!.close()
        canvas.drawPath(mPath!!, mPaint!!)
        val raidus = baseWidth / 8
        mPaint!!.style = Paint.Style.FILL
        canvas.drawCircle((x + baseWidth / 4 * 5 - raidus).toFloat(), (y + baseWidth / 4 * 5 + raidus * 2).toFloat(), raidus.toFloat(), mPaint!!)
        canvas.drawCircle((x + baseWidth / 2 - raidus).toFloat(), (y + baseWidth / 4 * 5 + raidus * 2).toFloat(), raidus.toFloat(), mPaint!!)
    }
}
