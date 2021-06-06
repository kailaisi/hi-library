package com.kailaisi.hiapp.route

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.kailaisi.common.ui.view.EmptyView
import com.kailaisi.hiapp.R

/**
 * 描述：
 * <p/>作者：wu
 * <br/>创建时间：2021-06-06:14:05
 */
@Route(path = "/degrade/global/activity")
class DegradeGlobalActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_global_error)
        val emptyView = findViewById<EmptyView>(R.id.empty_layout)
        emptyView.setIcon(R.string.if_unexpected1)
        emptyView.setTitle(getString(R.string.degrade_tips))
    }
}