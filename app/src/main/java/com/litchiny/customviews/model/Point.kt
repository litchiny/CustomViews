package com.litchiny.customviews.model

/**
 * author: LL .
 * date:   On 2019/1/23
 * caption:
 */

class Point @JvmOverloads constructor(var endX: Float, var endY: Float, realIndex: Int = -1, startIndex: Int = -1, isEmpty: Boolean = true, level: Int = -1) {
    var realIndex = -1    //实际的index位置
    var startIndex = -1   //初始的index的位置
    var isEmpty = true
    var isLongClick = false
    var level = -1       //颜色等级
    var areaArrayIndex = intArrayOf(-1, -1, -1, -1)
    var isAllowChange = true

    init {
        this.realIndex = realIndex
        this.startIndex = startIndex
        this.isEmpty = isEmpty
        this.level = level
    }

    override fun toString(): String {
        return "Point{" +
                //                "endX=" + endX +
                //                "endY=" + endY +
                "level=" + level +
                ", realIndex=" + realIndex +
                ", startIndex=" + startIndex +
                ", isEmpty=" + isEmpty +
                ", isAllowChange=" + isAllowChange +
                '}'.toString()
    }
}
