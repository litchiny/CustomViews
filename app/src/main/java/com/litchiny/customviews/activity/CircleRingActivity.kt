package com.litchiny.customviews.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.litchiny.customviews.R
import com.litchiny.customviews.model.CircleRingMode
import com.litchiny.customviews.view.CircleRingView
import java.util.*

class CircleRingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_circle_ring)
        initView()
    }

    private fun initView() {
        val crv_show = findViewById<CircleRingView>(R.id.crv_show)
        val sleepModes = ArrayList<CircleRingMode>()
        sleepModes.add(CircleRingMode(10f, ContextCompat.getColor(this, R.color.colorCircleRingType1)))
        sleepModes.add(CircleRingMode(5f, ContextCompat.getColor(this, R.color.colorCircleRingType2)))
        sleepModes.add(CircleRingMode(25f, ContextCompat.getColor(this, R.color.colorCircleRingType3)))
        sleepModes.add(CircleRingMode(60f, ContextCompat.getColor(this, R.color.colorCircleRingType4)))
        crv_show.setData(sleepModes)
    }
}
