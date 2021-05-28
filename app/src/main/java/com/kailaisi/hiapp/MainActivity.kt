package com.kailaisi.hiapp

import android.os.Bundle
import com.kailaisi.common.ui.component.HiBaseActivity
import com.kailaisi.hiapp.databinding.ActivityMainBinding
import com.kailaisi.hiapp.logic.MainActivityLogic
import com.kailaisi.library.util.HiDataBus

class MainActivity : HiBaseActivity(), MainActivityLogic.ActivityProvider {
    lateinit var logic: MainActivityLogic
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        logic = MainActivityLogic(this,savedInstanceState)
        HiDataBus.with<String>("stickdata").setStickyData("stickydata from main")
    }

    /**
     * 防止压后台之后，再回来导致的fragment重叠
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        logic.onSaveInstanceState(outState)
    }
}