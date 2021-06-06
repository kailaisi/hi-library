package com.kailaisi.hiapp.route

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.kailaisi.common.ui.view.EmptyView
import com.kailaisi.hiapp.R

/**
 * 描述：
 * <p/>作者：wu
 * <br/>创建时间：2021-06-06:14:05
 */
@Route(path = "/degrade/global/activity")
class DegradeGlobalActivity : AppCompatActivity() {
    @JvmField
    @Autowired
    var degrade_title: String? = null
    @JvmField
    @Autowired
    var degrade_des: String? = null
    @JvmField
    @Autowired
    var degrade_action: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ARouter.getInstance().inject(this)
        setContentView(R.layout.layout_global_error)
        val emptyView = findViewById<EmptyView>(R.id.empty_layout)
        degrade_title?.let {
            emptyView.setTitle(it)
        }
        degrade_des?.let {
            emptyView.setDesc(it)
        }
        degrade_action?.apply {
            emptyView.setHelpAction(listener = {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(this)))
            })
        }
        findViewById<View>(R.id.iv_back).setOnClickListener { onBackPressed() }
    }
}