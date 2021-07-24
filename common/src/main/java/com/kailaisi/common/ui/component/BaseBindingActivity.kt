package com.kailaisi.common.ui.component

import android.content.Intent
import android.os.Bundle

abstract class BaseBindingActivity : HiBaseActivity() {

    val TAG by lazy {
        javaClass.simpleName.toString()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    fun startActivity(clazz: Class<*>) {
        startActivity(Intent(this, clazz))
    }

    abstract fun initView()
}
