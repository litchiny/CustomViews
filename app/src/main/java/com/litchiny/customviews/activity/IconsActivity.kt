package com.litchiny.customviews.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import com.litchiny.customviews.R
import com.litchiny.customviews.view.BatteryView

/**
 * IconView的集合
 *
 * @author Litchiny
 */
class IconsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_icons)
        initView()
    }

    private fun initView() {
        val bv_title_battery = findViewById<BatteryView>(R.id.bv_title_battery)
        bv_title_battery.setData(30, 10)
    }
}
