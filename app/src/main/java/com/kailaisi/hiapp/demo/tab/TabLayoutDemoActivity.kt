package com.kailaisi.hiapp.demo.tab

import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kailaisi.hi_ui.tab.bottom.HiTabBottomInfo
import com.kailaisi.hiapp.R
import com.kailaisi.library.util.HiDisplayUtils
import kotlinx.android.synthetic.main.activity_tab_layout_demo.*

class TabLayoutDemoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tab_layout_demo)
        initTabBottom()
    }

    private fun initTabBottom() {
        val list = mutableListOf<HiTabBottomInfo<*>>()
        val home = HiTabBottomInfo(
            "首页",
            "fonts/iconfont.ttf",
            getString(R.string.if_home),
            null,
            "#ff656667",
            "#ffd44949"
        )
        var bitmap = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher, null)
        val cal = HiTabBottomInfo<String>(
            "收藏",
            bitmap, bitmap
        )
        val set = HiTabBottomInfo(
            "设置",
            "fonts/iconfont.ttf",
            getString(R.string.if_home),
            null,
            "#ff656667",
            "#ffd44949"
        )
        list.add(home)
        list.add(cal)
        list.add(set)
        layout.apply {
            setTabAlpha(0.85f)
            inflateInfo(list)
            addTabSelectedChangedListener { index, preInfo, nextInfo ->
                Toast.makeText(this@TabLayoutDemoActivity, nextInfo.name, Toast.LENGTH_SHORT).show()
            }
            defaultSelected(home)
        }
        var findTab = layout.findTab(cal)
        findTab?.resetHeight(HiDisplayUtils.dip2px(66f))
    }
}