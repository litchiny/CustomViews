package com.litchiny.customviews.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View

import com.litchiny.customviews.R
import com.litchiny.customviews.model.Point

import java.util.ArrayList

/**
 * author: Litchiny .
 * date:   On 2019/2/12
 * caption:
 */

class GameProposeView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {
    private val max_level = LEVEL_6
    private var radiusCircle2 = 100                                                               //直径
    private var paint: Paint? = null
    private var circleX = -1F
    private var circleY = -1F
    private var arrays: IntArray? = null
    private var mWidth: Int = 0
    private val circlePointList = HashMap<Int, Point>()                                      //圆圈的实际PointList,坐标位置可变
    private val finalSquarePointList = ArrayList<Point>()                                  //固定Square区域的Point,坐标位置不可变
    private var mHeight: Int = 0
    private var touchPoint: Point? = null                                                                       //实际触摸的那个Point（圆圈）
    private var textColor: Int = 0
    //    private var size = 3
    private var xPadding: Float = 0.toFloat()
    private var yPadding: Float = 0.toFloat()
    private var xLines = 5
    private var yLines = 10
    private var tempCircleX = circleX
    private var tempCircleY = circleY
    private var housePos: MutableMap<Int, Point> = HashMap()

    init {
        initPaint()
    }

    private fun initPaint() {
        val textSize = radiusCircle2 / 5
        paint = Paint()
        paint!!.isAntiAlias = true
        paint!!.strokeWidth = 1f
        paint!!.textSize = textSize.toFloat()
        paint!!.textAlign = Paint.Align.CENTER
        touchPoint = Point(-1f, -1f)
        textColor = Color.BLACK
        arrays = intArrayOf(ContextCompat.getColor(context, R.color.colorLevel0),
                ContextCompat.getColor(context, R.color.colorLevel1), ContextCompat.getColor(context, R.color.colorLevel2),
                ContextCompat.getColor(context, R.color.colorLevel3), ContextCompat.getColor(context, R.color.colorLevel4),
                ContextCompat.getColor(context, R.color.colorLevel5), ContextCompat.getColor(context, R.color.colorLevel6))
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mHeight = View.getDefaultSize(suggestedMinimumHeight, heightMeasureSpec)
        mWidth = View.getDefaultSize(suggestedMinimumWidth, widthMeasureSpec)
        radiusCircle2 = Math.min(mWidth, mHeight) / 7
        initList()
    }

    private fun initList() {
        clear()
        var index = 0
        xLines = mWidth / radiusCircle2 - 1
        yLines = mHeight / radiusCircle2 - 4
        xPadding = ((mWidth - radiusCircle2 * xLines) / 2).toFloat()
        yPadding = ((mHeight - radiusCircle2 * yLines) / 2).toFloat()
        for (j in 0 until yLines) {
            val endY = (j + 1) * radiusCircle2 + yPadding
            for (i in 0 until xLines) {
                val endX = (i + 1) * radiusCircle2 + xPadding
                val isEmpty = false
//                circlePointList.add(Point(endX - radiusCircle2 / 2, endY - radiusCircle2 / 2, index, index, isEmpty, if (isEmpty) -1 else LEVEL_0))
                circlePointList.put(index, Point(endX - radiusCircle2 / 2, endY - radiusCircle2 / 2, index, index, isEmpty, if (isEmpty) -1 else LEVEL_0))
                val pointF = Point(endX - radiusCircle2 / 2, endY - radiusCircle2 / 2)
                pointF.startIndex = index
                finalSquarePointList.add(pointF)     //固定坐标,不可移动.
                //暂时固定两个坐标
                index++
            }
        }
        val array = intArrayOf(7, 21)
        val tempList = circlePointList.clone() as HashMap<Int, Point>
        index = 0
        var housePoint = Point(-1F, -1F)
        for (i in 0 until tempList.size) {
            for (a in array) {
                if (i == a || i == (a + 1) || i == (a + 6) || i == a + 7) {
                    if (i == a + 7) {
                        housePoint = Point(tempList[i]?.endX ?: -1F, tempList[i]?.endY ?: -1F)
                        housePoint.areaArrayIndex = intArrayOf(a, a + 1, a + 6, a + 7)
                        housePoint.startIndex = index
                        Log.d(TAG, "housePoint: ${housePoint},i:$i")
                        housePos.put(index, housePoint)
                        index++
                    }
                    circlePointList.remove(i)
                    finalSquarePointList.get(i).isAllowChange = false
                }
            }
        }

//        size = finalSquarePointList.size
        Log.d(TAG, "initList: housePos: ${housePos.size},circlePointList: " + circlePointList.size +
                ",finalSquarePointList: " + finalSquarePointList.size)
    }

    fun clear() {
        circlePointList.clear()
        finalSquarePointList.clear()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_MOVE, MotionEvent.ACTION_DOWN -> {
                circleX = event.x
                circleY = event.y
                //不允许越过X,Y的最大边界
                circleX = if (circleX < radiusCircle2 / 2) radiusCircle2 / 2F else if (circleX > mWidth - radiusCircle2 / 2) mWidth - radiusCircle2 / 2F else circleX
                circleY = if (circleY < radiusCircle2 / 2) radiusCircle2 / 2F else if (circleY > mHeight - radiusCircle2 / 2) mHeight - radiusCircle2 / 2F else circleY
//                tempCircleX = circleX
//                tempCircleY = circleY
                updateMovePointPos()
            }
            MotionEvent.ACTION_UP -> updateUpPointPos()
        }
        return true
    }

    override fun onDraw(canvas: Canvas) {
        initDrawBackground(canvas)
        initDrawHouse(canvas)
        initDrawPoint(canvas)
    }

    private fun initDrawPoint(canvas: Canvas) {
        paint!!.style = Paint.Style.FILL
        var j = 0
        for (i in circlePointList.keys) {
            val point = circlePointList.get(i)
            if (point == null || point.isEmpty)//没有填充的内容
//            if (point == null)//没有填充的内容
                continue
            drawCircles(canvas, point, j)
            drawText(canvas, point)
            j++
        }
    }

    private fun initDrawBackground(canvas: Canvas) {
        paint!!.color = Color.BLACK
        paint!!.style = Paint.Style.STROKE
        if (tempCircleX > 0 && tempCircleY > 0)
            canvas.drawCircle(tempCircleX, tempCircleY, 200F, paint)
        for (i in 0 until xLines + 1) {
            canvas.drawLine(radiusCircle2 * i + xPadding, yPadding, radiusCircle2 * i + xPadding, mHeight - yPadding, paint!!)
        }

        for (i in 0 until yLines + 1) {
            canvas.drawLine(xPadding, radiusCircle2 * i + yPadding, mWidth - xPadding, radiusCircle2 * i + yPadding, paint!!)
        }
    }

    private fun initDrawHouse(canvas: Canvas) {
        if (housePos.isNotEmpty()) {
            paint!!.style = Paint.Style.FILL
            for (i in housePos.keys) {
                paint!!.color = Color.WHITE
                val housePoint = housePos.get(i)!!
                canvas.drawRect(housePoint.endX - radiusCircle2 * 3 / 2, housePoint.endY - radiusCircle2 * 3 / 2, housePoint.endX + radiusCircle2 / 2, housePoint.endY + radiusCircle2 / 2, paint)
                paint!!.color = Color.BLACK
                canvas.drawText("房子" + housePoint.startIndex, housePoint.endX - radiusCircle2 / 2, housePoint.endY - radiusCircle2 / 2, paint)
            }
        }
    }

    /**
     * 绘制所有的圆圈（9个）
     *
     * @param canvas
     * @param point
     */
    private fun drawCircles(canvas: Canvas, point: Point, j: Int) {
        val colorIndex = Math.min(returnColorIndex(point.level), arrays?.size!!)
        paint!!.color = if (point.isEmpty) Color.RED else arrays!![colorIndex]
        canvas.drawCircle(point.endX, point.endY, (radiusCircle2 / 5 * 2).toFloat(), paint!!)
    }

    /**
     * 辅助文字来查看实际以及原来的位置（S：Start Position，R：Real Position）
     *
     * @param canvas
     * @param point
     */
    private fun drawText(canvas: Canvas, point: Point) {
        paint!!.color = textColor
        canvas.drawText("S: " + point.startIndex.toString(), point.endX, point.endY - 15, paint!!)
        canvas.drawText("R: " + point.realIndex.toString(), point.endX, point.endY + 35, paint!!)
        canvas.drawText("Level: " + (Math.log(point.level.toDouble()) / Math.log(2.0)).toInt(), point.endX, point.endY + 10, paint!!)
    }

    private fun returnColorIndex(level: Int): Int {
        return (Math.log(level.toDouble()) / Math.log(2.0)).toInt() - 1
    }

    /**
     * 手势为ACTION_MOVE的时候，及时更新被Move的Point的位置
     */
    private fun updateMovePointPos() {
        if (touchPoint!!.startIndex >= 0) {
            val touchIndex = touchPoint!!.startIndex
            val point = circlePointList[touchIndex] ?: return
            updatePointListRealItem(point, touchIndex, point.realIndex, false)
            touchPoint = Point(point.endX, point.endY, point.realIndex, point.startIndex, true)
            postInvalidate()
            return
        }
        for (i in finalSquarePointList.indices) {
            val point = circlePointList[i]
            if (null == point || point.realIndex < 0 || point.isEmpty)    //只有原来状态是有圆圈的时候,才允许交换
                continue
            if (isCircleRange(point, false)) {
                //只有上一个圆圈结束了非正方形内的Move周期,才允许新的圆圈移动
                if (touchPoint!!.realIndex >= 0 && touchPoint!!.realIndex != point.realIndex)
                    continue
                updatePointListRealItem(point, i, point.realIndex, false)
                touchPoint = Point(point.endX, point.endY, point.realIndex, point.startIndex, true, -1)
                postInvalidate()
                break
            }
        }
    }

    /**
     * 手势为ACTION_UP的时候，更新被UP的Point的位置（需要检查是否是靠近Square的四周，如果是，则直接更新Point位置为固定的Square）
     */
    private fun updateUpPointPos() {
        Log.e(TAG, "112---touchPoint: " + touchPoint!!.toString())
        if (touchPoint!!.startIndex >= 0) {
            var point: Point
            var beChangedPoint: Point?  //被交换的
            var isChange = -1
            var isChangeLevel = false
            for (i in 0 until finalSquarePointList.size) {
                point = finalSquarePointList[i]
                if (!point.isAllowChange)
                    continue
                val result = isCircleRange(point, true)
                if (result) {                                           //已经靠近Rect区域周围
                    Log.d(TAG, "205---112---result:$result,i:$i,point:${point},circleX:$circleX,circleY:$circleY")
                    isChange = 0
                    circleX = point.endX
                    circleY = point.endY
                    val currentIndex = touchPoint!!.startIndex
                    val pointCurrent = circlePointList[currentIndex]
                    for (j in circlePointList.keys) {
                        beChangedPoint = circlePointList.get(j)
                        if (beChangedPoint == null)
                            continue
                        val index = touchPoint!!.realIndex  //实际的坐标位置
                        if (beChangedPoint.realIndex == point.startIndex && beChangedPoint.realIndex != pointCurrent?.realIndex && touchPoint!!.realIndex >= 0) {
                            val point3 = finalSquarePointList[index]           //这个是point1变更前的list内的index的值
                            beChangedPoint.endX = point3.endX
                            beChangedPoint.endY = point3.endY
                            beChangedPoint.realIndex = index
                            Log.d(TAG, "before---112---point2: ${beChangedPoint},\n pointCurrent: ${pointCurrent},\n touchPoint ${touchPoint},\n i:$i")
                            if (beChangedPoint.level == pointCurrent?.level && pointCurrent.level >= 0) {
                                beChangedPoint.level = -1;  //
                                beChangedPoint.isEmpty = true;
                                isChangeLevel = true;
                            }
                            Log.e(TAG, "112---point2: ${beChangedPoint},\n pointCurrent: ${pointCurrent},\n touchPoint ${touchPoint}")
                            break
                        }
                    }
                    updatePointListRealItem(pointCurrent, currentIndex, i, isChangeLevel)     //更新实际位于list位置的Point
                    clearTouchPoint()
                    break
                }
            }

            if (isChange < 0) {
                val point1 = circlePointList[touchPoint!!.startIndex]
                val point4 = finalSquarePointList[touchPoint!!.realIndex]
                if (point4.isAllowChange) {
                    point1?.endX = point4.endX
                    point1?.endY = point4.endY
                    Log.e(TAG, "112---isChange: $isChange,point1: ${point1},\n point4: ${point4},\n touchPoint ${touchPoint}")
                }
                clearTouchPoint()
            }
        }
    }

    /**
     * 更新Point的位置
     *
     * @param point     要更新的Point
     * @param index     实际list的位置
     * @param realIndex 在view的显示的位置,在手指抬起的时候可能会有变更
     */
    private fun updatePointListRealItem(point: Point?, index: Int, realIndex: Int, isChangeLevel: Boolean) {
        if (point == null)
            return
        point.endX = circleX
        point.endY = circleY
        point.realIndex = realIndex
        if (isChangeLevel) {
            point.level = point.level shl 1
            if (point.level > max_level) point.level = max_level
            Log.i(TAG, "最终更新的---112---point: " + point.toString())
        }
        circlePointList[index] = point
    }

    /**
     * Point是否处于可被拖动
     *
     * @param point
     * @param isFinal
     * @return
     */
    fun isCircleRange(point: Point, isFinal: Boolean): Boolean {
        val range = radiusCircle2 * 2 / 5
        return point.endX - range < circleX && circleX < point.endX + range &&
                point.endY - range < circleY && circleY < point.endY + range
    }

    private fun clearTouchPoint() {
        touchPoint!!.realIndex = -1
        touchPoint!!.startIndex = -1
        postInvalidate()
    }

    companion object {
        private val LEVEL_0 = 2
        private val LEVEL_1 = LEVEL_0 shl 1
        private val LEVEL_2 = LEVEL_1 shl 1
        private val LEVEL_3 = LEVEL_2 shl 1
        private val LEVEL_4 = LEVEL_3 shl 1
        private val LEVEL_5 = LEVEL_4 shl 1
        private val LEVEL_6 = LEVEL_5 shl 1
        private val TAG = "GameProposeView"
    }
}
