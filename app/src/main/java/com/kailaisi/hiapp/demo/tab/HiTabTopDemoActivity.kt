package com.kailaisi.hiapp.demo.tab

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.kailaisi.hi_ui.tab.top.HiTabTopInfo
import com.kailaisi.hiapp.R
import com.kailaisi.library.util.HiRes
import kotlinx.android.synthetic.main.activity_hi_tab_top_demo.*

class HiTabTopDemoActivity : AppCompatActivity() {
    val title = listOf("汽车", "关注", "百货", "家居", "装修", "运动", "科技", "企业","汽车", "关注", "百货", "家居", "装修", "运动", "科技", "企业")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hi_tab_top_demo)
        initTabBottom()
    }

    private fun initTabBottom() {
        val defaultColor = HiRes.getColor(R.color.tabTopDefaultColor)
        val tintColor = HiRes.getColor(R.color.tabTopTintColor)
        val list = title.map {
            HiTabTopInfo(it, defaultColor, tintColor)
        }
        tab_top_layout.apply {
            inflateInfo(list)
            addTabSelectedChangedListener { index, preInfo, nextInfo ->
                Toast.makeText(this@HiTabTopDemoActivity, nextInfo.name, Toast.LENGTH_SHORT).show()
            }
            defaultSelected(list[0])
        }
    }
}