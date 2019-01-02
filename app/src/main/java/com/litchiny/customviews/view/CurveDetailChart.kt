package com.litchiny.customviews.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.text.TextUtils
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.litchiny.customviews.R
import com.litchiny.customviews.model.ActivityDetailPoint
import com.litchiny.customviews.utils.AppUtil
import com.litchiny.customviews.utils.TimeUtil
import com.litchiny.customviews.utils.UnitUtil

import java.util.*
/**
 * 一个支持贝塞尔曲线图展示的view
 *
 * @author Litchiny
 */
class CurveDetailChart @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {
    private val TAG = CurveDetailChart::class.java.simpleName
    private var mPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mLinePaint: Paint = Paint()
    private var mLinePaint2: Paint = Paint()
    private var mTimeTextPaint: Paint = Paint()
    private var prevx: Float = 0.toFloat()
    private var curx: Float = 0.toFloat()
    private var prevd: Float = 0.toFloat()
    private var mode = NONE
    private var min_time: Long = 0
    private var max_time: Long = 0
    private var touch_time: Long = 0
    private var total_time: Long = 0
    private var static_min: Long = 0
    private var static_max: Long = 0
    private val pointList = ArrayList<Point>()
    private val dataLists = ArrayList<ActivityDetailPoint>()
    private var rate: Float = 0.toFloat()
    private var linex: Int = 0
    private var MAX_TIME: Long = 0
    private var MIN_TIME: Long = 0
    private val path = Path()
    private var layoutRect = Rect()
    private var showPadding = 0
    private var keyBase = 0
    private val lineNumY = 5
    private val showTypeYValueArray = 100
    private var unitIsMile: Boolean = false
    private var showTypeUnit = ""
    private val horizontalNum = 13
    private var distanceW: Int = 0
    private var distanceH: Int = 0
    private var lineStartX: Int = 0
    private var minValue = 0
    private var mValueLinePaint: Paint? = null
    private var mTextRectPaint: Paint? = null
    private var currentDescArray: List<String> = ArrayList()
    private var lineWidth: Int = 0
    private var tempPaddingStartX: Int = 0
    private var subtractPadding: Int = 0
    private var isShowTimeBottom: Boolean = false
    private var textWidth: Float = 0.toFloat()

    // 文字的高度
    private val textHeight: Float
        get() {
            val fm = mTimeTextPaint.fontMetrics
            return Math.ceil((fm.descent - fm.ascent).toDouble()).toFloat() - returnTextSize(2f, 6f)
        }

    init {
        initPaints()
    }

    private fun initPaints() {
        val textSize = returnTextSize(8f, 10f)
        mLinePaint = Paint()
        mLinePaint.isAntiAlias = true
        mLinePaint.strokeWidth = 0.5f
        mLinePaint.color = resources.getColor(R.color.colorRecyclerItemDividerBg)

        mLinePaint2 = Paint()
        mLinePaint2.isAntiAlias = true
        mLinePaint2.strokeWidth = 0.5f
        mLinePaint2.textSize = textSize.toFloat()
        mLinePaint2.textAlign = Paint.Align.CENTER
        mLinePaint2.pathEffect = DashPathEffect(floatArrayOf(4f, 4f), 0f)

        mTimeTextPaint = Paint()
        mTimeTextPaint.isAntiAlias = true
        mTimeTextPaint.textSize = textSize.toFloat()
        mTimeTextPaint.color = resources.getColor(R.color.colorTextCustomDeep)

        mPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FAKE_BOLD_TEXT_FLAG)
        mPaint.style = Paint.Style.FILL
        mPaint.textSize = textSize.toFloat()
        mPaint.textAlign = Paint.Align.LEFT
        linex = -1

        mTextRectPaint = Paint()
        mTextRectPaint?.isAntiAlias = true
        mTextRectPaint?.textAlign = Paint.Align.CENTER
        mTextRectPaint?.textSize = returnTextSize(10f, 12f).toFloat()
        mTextRectPaint?.color = resources.getColor(R.color.colorTextSelect)

        mValueLinePaint = Paint()
        mValueLinePaint?.isAntiAlias = true
        lineWidth = UnitUtil.dp2px(context, 1.5f)
        textWidth = getTextWidth(mLinePaint2, "Max 180").toFloat()
    }

    private fun returnTextSize(small: Float, big: Float): Int {
        return UnitUtil.dp2px(context, small)
    }

    fun setData(datasList: List<ActivityDetailPoint>, minTime: Int, maxTime: Int, isShowTimeBottom: Boolean) {
        this.MAX_TIME = maxTime.toLong()
        this.MIN_TIME = minTime.toLong()
        if (this.dataLists.size > 0) this.dataLists.clear()
        if (this.pointList.size > 0) this.pointList.clear()
        var maxYPointValue = 0f
        if (datasList.size > 0) {
            this.dataLists.addAll(datasList)
            val yValueList = ArrayList<Float>()
            for (point in datasList) {
                yValueList.add(point.avg)
            }
            maxYPointValue = Collections.max(yValueList)
        }
        val dayStrArray = if (isShowTimeBottom)
            arrayOf("0", "2", "4", "6", "8", "10", "12", "14", "16", "18", "20", "22")
        else
            arrayOf("00:00", "02:00", "04:00", "06:00", "08:00", "10:00", "12:00", "14:00", "16:00", "18:00", "20:00", "22:00")

        currentDescArray = Arrays.asList<String>(*dayStrArray)
        maxYPointValue = if (maxYPointValue == 0f) (showTypeYValueArray * lineNumY).toFloat() else maxYPointValue
        var keyBase: Int = (maxYPointValue / lineNumY).toInt()
        if (keyBase * lineNumY - maxYPointValue < keyBase)
            keyBase = ((maxYPointValue + keyBase) / lineNumY).toInt()
        this.showTypeUnit = ""
        this.minValue = 0

        this.linex = 0
        this.keyBase = keyBase
        this.isShowTimeBottom = isShowTimeBottom
        init()
        rate = 1f
    }

    private fun init() {
        max_time = MAX_TIME
        min_time = MIN_TIME
        total_time = max_time - min_time
        static_min = min_time
        static_max = max_time
        if (pointList.size > 0)
            pointList.clear()
        convertData2Point()
        postInvalidate()
    }

    private fun convertData2Point() {
        if (dataLists.size > 0) {
            val rate1 = returnRate()
            val keyValueY = distanceH / (lineNumY + 1f)
            for (rateData in dataLists) {
                pointList.add(Point(rate1 * (rateData.timeStamp - min_time) + lineStartX.toFloat() + tempPaddingStartX.toFloat(), returnDataYValue(rateData.avg, keyValueY)))
            }
        }
    }

    //控制x坐标之间的间隔
    private fun returnRate(): Float {
        var dex = 25f
        dex = if (isShowTimeBottom) (distanceW.toFloat() - tempPaddingStartX.toFloat() - showPadding * 3f) / (max_time - min_time) else (distanceW + lineStartX * dex) / (max_time - min_time)
        return dex
    }

    private fun returnDataYValue(value: Float, keyValueY: Float): Float {
        val padding = if (isShowTimeBottom) showPadding else 0
        return distanceH - keyValueY * (value / 1f / keyBase + 0.5f) + keyValueY * minValue / keyBase - padding
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val lastcurx: Float
        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                mode = DRAG
                prevx = event.x
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                if (mode == DRAG) {
                    lastcurx = curx
                    curx = event.x
                    var temp_min_time = ((total_time / distanceW).toFloat() * (prevx - curx).toInt() / 2).toInt() + static_min
                    temp_min_time = if (temp_min_time < MIN_TIME) MIN_TIME else temp_min_time
                    val temp_max_time = temp_min_time + total_time
                    if (isShowTimeBottom) {
                        if (temp_max_time > MAX_TIME) {
                            return false
                        }
                    }

                    if (max_time > MAX_TIME + total_time * 0.6f && curx < lastcurx && curx < prevx && lastcurx > 0) {
                        return false
                    }
                    val num = (max_time - min_time) * 10f / (MAX_TIME - MIN_TIME).toFloat() / 10f
                    if (num >= 0.5f && temp_max_time > MAX_TIME + (MAX_TIME - MIN_TIME) / 1.6f ||
                            num >= 0.2f && num < 0.5f && temp_max_time > MAX_TIME + (MAX_TIME - MIN_TIME) * num ||
                            num < 0.2f && temp_max_time > MAX_TIME + (MAX_TIME - MIN_TIME) / 10) {
                        return false
                    }

                    if (Math.abs(temp_max_time - temp_min_time) > (MAX_TIME - MIN_TIME) / 24) {
                        min_time = temp_min_time
                        max_time = temp_max_time
                    }

                } else if (mode == ZOOM) {
                    val curd = spacing(event)
                    val lastRate = rate
                    rate = prevd / curd
                    rate = if (rate > 10f) 10f else if (rate < 0.7f) 0.7f else rate
                    if (lastRate == 0.7f && lastRate == rate ||
                            static_min > static_max ||
                            (static_max - static_min) * rate < (MAX_TIME - MIN_TIME) / 6) {                                // 最大值与最小值之差小于2小时，不再缩放了
                        return false
                    }
                    changeTimeRange()
                }
                pointList.clear()
                convertData2Point()
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                static_max = max_time
                static_min = min_time
                linex = getClosestValueIndex(prevx)
            }
            MotionEvent.ACTION_POINTER_UP -> mode = NONE
            MotionEvent.ACTION_POINTER_DOWN -> {
                linex = -1
                touch_time = x2Timestamp((event.getX(0) + event.getX(1)).toInt() / 2f)
                mode = ZOOM
                if (event.pointerCount > 1) {
                    prevd = spacing(event)
                }
            }
        }
        return true
    }

    override fun onDraw(canvas: Canvas) {
        drawXAxis(canvas)
        drawYAxis(canvas)
        drawBeizer(canvas)
        drawTouch(canvas)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val sizeWidth = View.MeasureSpec.getSize(widthMeasureSpec)
        val sizeHeight = View.MeasureSpec.getSize(heightMeasureSpec)
        var resultWidth = sizeWidth
        var resultHeight = sizeHeight
        // 考虑内边距对尺寸的影响
        resultWidth += paddingLeft + paddingRight
        resultHeight += paddingTop + paddingBottom
        // 考虑父容器对尺寸的影响
        resultWidth = resolveMeasure(sizeWidth, resultWidth)
        distanceW = resultWidth
        resultHeight = resolveMeasure(sizeHeight, resultHeight)
        distanceH = resultHeight
        setMeasuredDimension(resultWidth, resultHeight)
        val num = if (isShowTimeBottom) horizontalNum - 2 else horizontalNum + 2
        showPadding = distanceW / num
        lineStartX = (distanceW - showPadding * (num - 3)) / 2
        tempPaddingStartX = if (isShowTimeBottom) lineStartX / 8 else lineStartX / 5 * 4
        subtractPadding = if (isShowTimeBottom) showPadding else 0
        val rectBottom = if (isShowTimeBottom) distanceH else showPadding
        layoutRect = Rect(0, 0, distanceW, rectBottom)
        init()
    }

    /**
     * 根据传入的值进行测量
     */
    fun resolveMeasure(measureSpec: Int, defaultSize: Int): Int {
        var result = defaultSize
        val specSize = View.MeasureSpec.getSize(measureSpec)
        when (View.MeasureSpec.getMode(measureSpec)) {
            View.MeasureSpec.AT_MOST, View.MeasureSpec.EXACTLY -> result = Math.min(specSize, defaultSize)
        }
        return result
    }

    private fun spacing(event: MotionEvent): Float {
        val x = event.getX(0) - event.getX(1)
        val y = event.getY(0) - event.getY(1)
        return Math.sqrt((x * x + y * y).toDouble()).toFloat()
    }

    private fun changeTimeRange() {
        min_time = (touch_time - (touch_time - static_min) * rate).toLong()
        max_time = (touch_time + (static_max - touch_time) * rate).toLong()
        if (min_time <= MIN_TIME) min_time = MIN_TIME
        if (max_time >= MAX_TIME) max_time = MAX_TIME
        total_time = max_time - min_time
    }

    private fun getClosestValueIndex(x: Float): Int {
        var res = -1
        if (pointList.size > 0) {
            var dif = (distanceW / pointList.size).toFloat()
            var tmp: Float
            for (i in pointList.indices) {
                tmp = Math.abs(x - pointList[i].px)
                if (tmp < dif) {
                    dif = tmp
                    res = i
                }
            }
        }
        return res
    }

    private fun x2Timestamp(x: Float): Long {
        return ((max_time - min_time) * (x / distanceW) + min_time).toLong()
    }

    // 画x轴
    private fun drawXAxis(canvas: Canvas) {
        val rate1 = returnRate()
        val y = layoutRect.bottom - 0.5f * textHeight
        mTimeTextPaint.textAlign = Paint.Align.CENTER
        val showNum = currentDescArray.size
        var timeStamp: Long
        val dex = 2
        for (i in 0 until showNum) {
            timeStamp = 60 * 60 * i * dex + MIN_TIME
            if (timeStamp >= min_time) {
                val x = rate1 * (timeStamp - min_time) + lineStartX.toFloat() + tempPaddingStartX.toFloat()
                canvas.drawText(currentDescArray[i], x, y, mTimeTextPaint)
            }
        }
    }

    fun returnFormatNum(showStr: String): String? {
        if (TextUtils.isEmpty(showStr))
            return showStr
        return if (showStr.endsWith("0")) if (showStr.contains(",")) showStr.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0] else showStr.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0] else showStr
    }

    // 画Y轴及线
    private fun drawYAxis(canvas: Canvas) {
        mPaint.style = Paint.Style.FILL
        mPaint.strokeWidth = 5f
        mPaint.color = resources.getColor(R.color.colorTextCustomDeep)

        val keyValueY = (distanceH / (lineNumY + 1)).toFloat()
        var yValue: Float
        val base = keyBase
        var showStr: String?
        for (i in 0 until lineNumY) {
            yValue = keyValueY * (i + 2) - subtractPadding
            showStr = returnFormatNum((base * (lineNumY - i - 1) + minValue).toString())
            if (i < lineNumY - 1) {
                showStr += showTypeUnit
                canvas.drawLine((lineStartX - showPadding).toFloat(), yValue, (distanceW - lineStartX + showPadding).toFloat(), yValue, mLinePaint!!)
            }
            canvas.drawText(showStr!!, (lineStartX - showPadding).toFloat(), yValue + 0.5f * textHeight - keyValueY / 2, mPaint!!)
        }
        if (isShowTimeBottom) {
            yValue = (distanceH - showPadding).toFloat()
            canvas.drawLine(0f, yValue, distanceW.toFloat(), yValue, mLinePaint!!)
        }
    }

    private fun drawBeizer(canvas: Canvas) {
        if (pointList.isNotEmpty()) {
            mPaint.style = Paint.Style.STROKE
            mPaint.strokeWidth = lineWidth.toFloat()
            mPaint.color = resources.getColor(R.color.colorPrimaryDark)
            var startp: Point
            var endp: Point
            for (i in 0 until pointList.size - 1) {
                startp = pointList[i]
                endp = pointList[i + 1]
                val wt = (startp.px + endp.px) / 2
                path.reset()
                path.moveTo(startp.px, startp.py)
                path.cubicTo(wt, startp.py, wt, endp.py, endp.px, endp.py)
                canvas.drawPath(path, mPaint)
            }
        }
    }

    private fun drawTouch(canvas: Canvas) {
        if (pointList.size > 0 && linex >= 0 && linex < pointList.size) {
            val hAndMArray = TimeUtil.timeStampToString(dataLists[linex].timeStamp).split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (hAndMArray.size > 1) {//0 点
                val x = pointList[linex].px - textHeight
                val y = pointList[linex].py - textHeight
                val bitmap = BitmapFactory.decodeResource(resources, R.mipmap.show_point)
                val height = bitmap.height
                //                LogUtil.i(TAG, "height: " + bitmap.getHeight() + ",width: " + bitmap.getWidth());
                canvas.drawBitmap(bitmap, x, y, mValueLinePaint)
                canvas.drawBitmap(BitmapFactory.decodeResource(resources, R.mipmap.show_value_bg), x - height, y - 2 * height, mValueLinePaint)
                canvas.drawText(returnFormatNum(dataLists[linex].avg.toString())!!, x + height / 2, y - height + 1, mTextRectPaint!!)
            }
        }
    }

    private inner class Point internal constructor(internal var px: Float, internal var py: Float) {

        override fun toString(): String {
            return "px:$px, py:$py"
        }
    }

    companion object {

        private val NONE = 0
        private val DRAG = 1
        private val ZOOM = 2

        fun getTextWidth(paint: Paint, str: String?): Int {
            var iRet = 0
            if (str != null && str.length > 0) {
                val len = str.length
                val widths = FloatArray(len)
                paint.getTextWidths(str, widths)
                for (j in 0 until len) {
                    iRet += Math.ceil(widths[j].toDouble()).toInt()
                }
            }
            return iRet
        }
    }
}
