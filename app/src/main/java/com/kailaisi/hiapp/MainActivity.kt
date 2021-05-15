package com.kailaisi.hiapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kailaisi.hi_ui.tab.bottom.HiTabBottom
import com.kailaisi.hi_ui.tab.bottom.HiTabBottomInfo
import com.kailaisi.hiapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        val data=HiTabBottomInfo("首页",
            "fonts/iconfont.ttf",
            getString(R.string.if_home),
            null,
            "#ff656667",
            "#ffd44949"
                 )
        mBinding.tabBottom.setHiTabInfo(data)
    }
}