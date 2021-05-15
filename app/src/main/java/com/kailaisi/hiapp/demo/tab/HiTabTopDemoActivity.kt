package com.kailaisi.hiapp.demo.tab

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.kailaisi.hi_ui.tab.top.HiTabTopInfo
import com.kailaisi.hiapp.R
import com.kailaisi.hiapp.databinding.ActivityHiTabTopDemoBinding

class HiTabTopDemoActivity : AppCompatActivity() {
    val title = listOf("汽车", "关注", "百货", "家居", "装修", "运动", "科技", "企业","汽车", "关注", "百货", "家居", "装修", "运动", "科技", "企业")
    val binding by lazy { ActivityHiTabTopDemoBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initTabBottom()
    }

    private fun initTabBottom() {
        val defaultColor = resources.getColor(R.color.tabTopDefaultColor)
        val tintColor = resources.getColor(R.color.tabTopTintColor)
        val list = title.map {
            HiTabTopInfo(it, defaultColor, tintColor)
        }
        var layout = binding.tabTopLayout
        layout.apply {
            inflateInfo(list)
            addTabSelectedChangedListener { index, preInfo, nextInfo ->
                Toast.makeText(this@HiTabTopDemoActivity, nextInfo.name, Toast.LENGTH_SHORT).show()
            }
            defaultSelected(list[0])
        }
    }
}