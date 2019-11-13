package com.litchiny.customviews.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import com.litchiny.customviews.R
import com.litchiny.customviews.model.ActivityDetailPoint
import com.litchiny.customviews.utils.TimeUtil
import com.litchiny.customviews.view.CurveDetailChart

import java.util.ArrayList
import java.util.Calendar

class CurveDetailChartActivity : AppCompatActivity() {
    private var pointDayList: MutableList<ActivityDetailPoint>? = null
    private var startTime: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_curve_detail_chart)
        initDayList()
        initView()
    }

    private fun initDayList() {
        pointDayList = ArrayList()
        val time = System.currentTimeMillis() / 1000L
        startTime = (TimeUtil.resetDayStart(Calendar.getInstance()).timeInMillis / 1000).toInt()
        pointDayList!!.add(ActivityDetailPoint((startTime + 60 * 60 * 1).toLong(), 56f))
        pointDayList!!.add(ActivityDetailPoint((startTime + 60 * 60 * 2).toLong(), 67f))
        pointDayList!!.add(ActivityDetailPoint((startTime + 60 * 60 * 3).toLong(), 134f))
        pointDayList!!.add(ActivityDetailPoint((startTime + 60 * 60 * 4).toLong(), 90f))
        pointDayList!!.add(ActivityDetailPoint((startTime + 60 * 60 * 5).toLong(), 50f))
        pointDayList!!.add(ActivityDetailPoint((startTime + 60 * 60 * 6).toLong(), 180f))
        pointDayList!!.add(ActivityDetailPoint((startTime + 60 * 60 * 7).toLong(), 80f))
        pointDayList!!.add(ActivityDetailPoint((startTime + 60 * 60 * 8).toLong(), 50f))
        pointDayList!!.add(ActivityDetailPoint((startTime + 60 * 60 * 9).toLong(), 110f))
        pointDayList!!.add(ActivityDetailPoint((startTime + 60 * 60 * 10).toLong(), 140f))
        pointDayList!!.add(ActivityDetailPoint((startTime + 60 * 60 * 11).toLong(), 240f))
        pointDayList!!.add(ActivityDetailPoint((startTime + 60 * 60 * 17).toLong(), 54f))
        pointDayList!!.add(ActivityDetailPoint((startTime + 60 * 60 * 21).toLong(), 89f))
        pointDayList!!.add(ActivityDetailPoint((startTime + 60 * 60 * 23).toLong(), 156f))
    }

    private fun initView() {
        val cdc_show = findViewById<CurveDetailChart>(R.id.cdc_show)
        cdc_show.setData(pointDayList!!, startTime, startTime + 23 * 59 * 59, true)
    }
}
