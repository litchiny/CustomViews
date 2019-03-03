package com.litchiny.customviews

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.litchiny.customviews.activity.*
import com.litchiny.customviews.adapter.TextAdapter

/**
 * 主Activity
 *
 * @author Litchiny
 */
class MainActivity : AppCompatActivity() {

    private lateinit var adapter: TextAdapter
    private lateinit var context: Context;
    private val array = arrayListOf<String>("FlowLayout", "CircleRing","CurveDetailChart","Icons","GamePropose")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        val rv_show = findViewById<RecyclerView>(R.id.rv_show)
        rv_show.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        rv_show.setHasFixedSize(true)
        rv_show.addItemDecoration(DividerItemDecoration(context,DividerItemDecoration.VERTICAL))
        adapter = TextAdapter()
        adapter.bindData(array)

        adapter.setItemClickListener {
            when (it) {
                0 -> startActivity(Intent(this@MainActivity, FlowLayoutActivity::class.java))
                1 -> startActivity(Intent(this@MainActivity, CircleRingActivity::class.java))
                2 -> startActivity(Intent(this@MainActivity, CurveDetailChartActivity::class.java))
                3 -> startActivity(Intent(this@MainActivity, IconsActivity::class.java))
                4 -> startActivity(Intent(this@MainActivity, GameProposeActivity::class.java))
            }
        }
        rv_show.adapter = adapter
    }


}
