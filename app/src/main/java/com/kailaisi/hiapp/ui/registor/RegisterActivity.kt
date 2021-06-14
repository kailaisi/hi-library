package com.kailaisi.hiapp.ui.registor

import android.graphics.Color
import com.alibaba.android.arouter.facade.annotation.Route
import com.kailaisi.hiapp.BaseBindingActivity
import com.kailaisi.hiapp.databinding.ActivityRegisterBinding
import com.kailaisi.library.util.HiStatusBar

@Route(path = "/account/register")
class RegisterActivity : BaseBindingActivity<ActivityRegisterBinding>() {
    override fun initView() {
        HiStatusBar.setStatusBar(this,true, Color.WHITE)
    }

    override fun getBinding(): ActivityRegisterBinding {
        return ActivityRegisterBinding.inflate(layoutInflater)
    }
}