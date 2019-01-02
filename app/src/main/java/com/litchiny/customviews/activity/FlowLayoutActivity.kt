package com.litchiny.customviews.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.litchiny.customviews.R

import com.litchiny.customviews.view.FlowLayout

import java.util.ArrayList
import java.util.Arrays

/**
 * 流式布局
 *
 * @author Litchiny
 */
class FlowLayoutActivity : AppCompatActivity() {
    private var fl_layout: FlowLayout? = null
    private val datas = ArrayList<String>()
    private val testArray = arrayOf("add1", "fgdjk", "yriwyfsfd", "1", "hi", "lemon", "hahah")
    private val endStr = "+"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flow)
        initData()
        initView()
    }

    private fun initData() {
        val arrays = arrayOf("orange", "phone", "computer", "vegetable", "litchi", "apple", endStr)
        datas.addAll(Arrays.asList(*arrays))
    }

    private fun initView() {
        fl_layout = findViewById(R.id.fl_layout)
        fl_layout!!.setData(datas, true, 0)
        fl_layout!!.setFlowClickListener(object : FlowLayout.FlowClickListener {
            override fun setOnClickItemListener(index: Int) {
                Log.d(TAG, "setOnClickItemListener: index: $index")
                if (index == datas.size - 1) {
                    val lastSize = datas.size - 1
                    val addStr = testArray[(Math.random() * testArray.size - 1).toInt()]
                    datas[lastSize] = addStr
                    datas.add(endStr)
                    fl_layout!!.addData(addStr, false)
                }
            }

            override fun setOnClickLongItemListener(index: Int) {
                if (index == datas.size - 1) {
                    return
                }
                datas.removeAt(index)
                fl_layout!!.removeItem(index)
                if (datas.size == 1) {
                    fl_layout!!.clearDeleteType()
                }
            }
        })
    }

    companion object {
        private val TAG = "FlowLayoutActivity"
    }
}
