package com.litchiny.customviews.view


import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.litchiny.customviews.R
import java.util.*

/**
 * 一个流式布局view
 *
 * @author Litchiny
 */
class FlowLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {
    private var mPaint: Paint? = null
    private val textList = ArrayList<String>()
    private val minPadding = 100
    private var textPadding = 20
    private var textSize = 30f
    private var textPace = 35                                //两个item的间隔
    private var textHeight = 40
    private var distanceW = 1080
    private val pointList = ArrayList<IntArray>()
    private var clickIndex = -1
    private var touchDownTime: Long = 0
    var isLongClick: Boolean = false
        private set
    private val textNormalColor: Int
    private val textClickColor: Int
    private var isShowEndAdd: Boolean = false                            //最后一个字符串是否是+
    private var listener: FlowClickListener? = null
    private var paddingHasChanged: Boolean = false
    private var minHeight = 500
    private var baseItemHeight: Int = 0
    private var totalTextWidth: Int = 0

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.FlowLayout)
        textNormalColor = a.getColor(R.styleable.FlowLayout_flow_text_normal_color, Color.BLUE)
        textClickColor = a.getColor(R.styleable.FlowLayout_flow_text_click_color, Color.WHITE)
        textSize = a.getDimension(R.styleable.FlowLayout_flow_text_size, context.resources.getDimension(R.dimen.dimen_size_15))
        initPaint()
    }

    private fun initPaint() {
        mPaint = Paint()
        mPaint!!.textSize = textSize
        val rect = Rect()
        mPaint!!.getTextBounds("1", 0, "1".length, rect)
        textHeight = rect.height()
        textPadding = textHeight
        textPace = textHeight * 3 / 2
        baseItemHeight = textHeight + textPadding * 2 + textPace
        mPaint!!.isAntiAlias = true
    }

    @JvmOverloads
    fun setData(textList: List<String>, isShowEndAdd: Boolean = false, clickIndex: Int = -1) {
        this.isShowEndAdd = isShowEndAdd
        this.clickIndex = clickIndex
        this.textList.clear()
        this.textList.addAll(textList)
        resetMinHeight()
        postInvalidate()
    }

    //重设最小高度
    private fun resetMinHeight() {
        val lastMinHeight = minHeight
        minHeight = minPadding
        var totalTextWidthC = 0
        for (i in textList.indices) {
            val width = mPaint!!.measureText(textList[i])
            totalTextWidthC += (width + (textPadding * 2).toFloat() + textPace.toFloat()).toInt()
            if (totalTextWidthC + textPace > distanceW) {  //判断width 是否够
                totalTextWidthC = (width + (textPadding * 2).toFloat() + textPace.toFloat()).toInt()
                minHeight += baseItemHeight
            }
            if (i == textList.size - 1)
                this.totalTextWidth = totalTextWidthC
        }
        minHeight += textPadding * 2
        if (minHeight != lastMinHeight) {
            requestLayout()
        }
    }

    /**
     * @param addStr
     */
    fun addData(addStr: String) {
        if (!isShowEndAdd) {    //新增加的字符串是否显示在末尾
            clickIndex = this.textList.size
            this.textList.add(addStr)
            //TODO  待补充requestLayout逻辑
        } else {
            val lastListSize = textList.size - 1
            clickIndex = lastListSize
            val endStr = textList[lastListSize]
            textList[lastListSize] = addStr
            textList.add(endStr)
            val lastWidth = mPaint!!.measureText(endStr)
            val addWidth = mPaint!!.measureText(addStr)
            val lastMinHeight = minHeight
            var totalTextWidthC = (totalTextWidth + (addWidth - lastWidth)).toInt()
            for (i in 0..1) {
                if (i > 0)
                    totalTextWidthC += (lastWidth + (textPadding * 2).toFloat() + textPace.toFloat()).toInt()
                if (totalTextWidthC + textPace > distanceW) {  //判断width 是否够
                    totalTextWidthC = ((if (i == 0) addWidth else lastWidth) + (textPadding * 2).toFloat() + textPace.toFloat()).toInt()
                    minHeight += baseItemHeight
                }
                if (i == 1)
                    this.totalTextWidth = totalTextWidthC
            }
            if (minHeight != lastMinHeight) {
                requestLayout()
            }
        }
        postInvalidate()
    }

    fun clearDeleteType() {
        clickIndex = -1
        isLongClick = false
        postInvalidate()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_UP -> {
                if (paddingHasChanged) {
                    postInvalidate()
                    paddingHasChanged = false
                }
                if (System.currentTimeMillis() - touchDownTime > 500) {
                    if (null != listener) {
                        clickIndex = -1
                        isLongClick = !isLongClick
                        postInvalidate()
                    }
                } else {
                    var i = 0
                    val x = event.x
                    val y = event.y
                    for (arr in pointList) {
                        if (x >= arr[0] && x <= arr[2] && y >= arr[1] && y <= arr[3]) {
                            clickIndex = i
                            break
                        }
                        i++
                    }

                    if (null != listener && clickIndex >= 0) {
                        if (isLongClick)
                            listener!!.setOnClickLongItemListener(clickIndex)
                        else
                            listener!!.setOnClickItemListener(clickIndex)
                        postInvalidate()
                    }
                }
            }

            MotionEvent.ACTION_DOWN -> {
                paddingHasChanged = false
                touchDownTime = System.currentTimeMillis()
            }
        }
        return true
    }

    override fun onDraw(canvas: Canvas) {
        if (textList.size <= 0)
            return
        var startTextTop = minPadding
        var startTextLeft = 0
        var totalTextWidth = 0
        pointList.clear()
        mPaint!!.textSize = textSize
        for (i in textList.indices) {
            mPaint!!.textAlign = Paint.Align.LEFT
            val width = mPaint!!.measureText(textList[i])
            val lastWidth = totalTextWidth
            totalTextWidth += (width + (textPadding * 2).toFloat() + textPace.toFloat()).toInt()
            if (totalTextWidth + textPace > distanceW) {  //判断width 是否够
                totalTextWidth = (width + (textPadding * 2).toFloat() + textPace.toFloat()).toInt()
                startTextLeft = textPace
                startTextTop += baseItemHeight
            } else {
                startTextLeft = lastWidth + textPace
            }

            mPaint!!.color = textNormalColor
            val isShowClickType = i == clickIndex && null != listener && (!isShowEndAdd || i != textList.size - 1) && !paddingHasChanged
            mPaint!!.style = if (isShowClickType) Paint.Style.FILL else Paint.Style.STROKE
            val array = intArrayOf(startTextLeft - textPadding, startTextTop - textPadding - textHeight, (startTextLeft.toFloat() + width + textPadding.toFloat()).toInt(), startTextTop + textPadding)
            canvas.drawRect(array[0].toFloat(), array[1].toFloat(), array[2].toFloat(), array[3].toFloat(), mPaint!!)
            mPaint!!.color = if (isShowClickType) textClickColor else textNormalColor
            canvas.drawText(textList[i], startTextLeft.toFloat(), startTextTop.toFloat(), mPaint!!)
            if (isLongClick && null != listener && (!isShowEndAdd || i != textList.size - 1) && !paddingHasChanged) {
                mPaint!!.style = Paint.Style.FILL_AND_STROKE
                mPaint!!.color = textClickColor
                canvas.drawCircle(startTextLeft.toFloat() + width + textPadding.toFloat(), (startTextTop - textPadding * 2).toFloat(), textPadding.toFloat(), mPaint!!)
                mPaint!!.color = textNormalColor
                mPaint!!.style = Paint.Style.STROKE
                canvas.drawCircle(startTextLeft.toFloat() + width + textPadding.toFloat(), (startTextTop - textPadding * 2).toFloat(), textPadding.toFloat(), mPaint!!)
                mPaint!!.textAlign = Paint.Align.CENTER
                canvas.drawText("x", startTextLeft.toFloat() + width + textPadding.toFloat(), (startTextTop - textPadding * 2 + textHeight / 2).toFloat(), mPaint!!)
                val part = textPadding + 5
                array[1] -= part
                array[2] += part
                array[0] = array[2] - part * 2
                array[3] = array[1] + part * 2
            }
            pointList.add(array)
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        Log.d(TAG, "onLayout: ")
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
        Log.d(TAG, "onMeasure: minHeight: $minHeight,resultHeight: $resultHeight")
        setMeasuredDimension(resultWidth, minHeight)
    }

    /**
     * 根据传入的值进行测量
     */
    fun resolveMeasure(measureSpec: Int, defaultSize: Int): Int {
        var result = 0
        val specSize = View.MeasureSpec.getSize(measureSpec)
        when (View.MeasureSpec.getMode(measureSpec)) {
            View.MeasureSpec.AT_MOST, View.MeasureSpec.EXACTLY ->
                //设置warp_content时设置默认值
                result = Math.min(specSize, defaultSize)
            else -> result = defaultSize
        }
        return result
    }

    fun removeItem(index: Int) {
        textList.removeAt(index)
        clickIndex = -1
        resetMinHeight()
        postInvalidate()
    }

    fun setFlowClickListener(clickListener: FlowClickListener) {
        this.listener = clickListener
    }

    interface FlowClickListener {
        fun setOnClickItemListener(index: Int)

        fun setOnClickLongItemListener(index: Int)
    }

    companion object {
        private val TAG = "FlowLayout"
    }
}