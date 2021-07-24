package com.kailaisi.biz_login

import android.graphics.Color
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.kailaisi.common.ui.component.HiBaseActivity
import com.kailaisi.library.util.HiStatusBar

@Route(path = "/account/register")
class RegisterActivity : HiBaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        HiStatusBar.setStatusBar(this, true, Color.WHITE)
        setContentView(R.layout.activity_register)
    }
}