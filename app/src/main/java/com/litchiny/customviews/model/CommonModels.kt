package com.litchiny.customviews.model

import android.graphics.Color


/**
 * 所有model的集合
 *
 * @author Litchiny
 */
class ActivityDetailPoint(var timeStamp: Long, var avg: Float) {

    override fun toString(): String {
        return "ActivityDetailPoint{" +
                "timeStamp=" + timeStamp +
                ", avg=" + avg +
                '}'
    }
}

class CircleRingMode(var progress: Float, var colorValue: Int) {
    var startTime: Long = 0
}