package com.kailaisi.hiapp.demo.tab

import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.kailaisi.hi_ui.tab.bottom.HiDisplayUtils
import com.kailaisi.hi_ui.tab.bottom.HiTabBottomInfo
import com.kailaisi.hiapp.R
import com.kailaisi.hiapp.databinding.ActivityTabLayoutDemoBinding

class TabLayoutDemoActivity : AppCompatActivity() {

    val binding by lazy { ActivityTabLayoutDemoBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
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
            bitmap,bitmap
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
        var layout = binding.layout
        layout.apply {
            setTabAlpha(0.85f)
            inflateInfo(list)
            addTabSelectedChangedListener { index, preInfo, nextInfo ->
                Toast.makeText(this@TabLayoutDemoActivity, nextInfo.name, Toast.LENGTH_SHORT).show()
            }
            defaultSelected(home)
        }
        var findTab = layout.findTab(cal)
        findTab?.resetHeight(HiDisplayUtils.dip2px(this,66f))
    }
}