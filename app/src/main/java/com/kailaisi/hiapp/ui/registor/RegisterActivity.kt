package com.kailaisi.hiapp.ui.registor

import com.alibaba.android.arouter.facade.annotation.Route
import com.kailaisi.hiapp.BaseBindingActivity
import com.kailaisi.hiapp.databinding.ActivityRegisterBinding

@Route(path = "/account/register")
class RegisterActivity : BaseBindingActivity<ActivityRegisterBinding>() {
    override fun initView() {
    }

    override fun getBinding(): ActivityRegisterBinding {
        return ActivityRegisterBinding.inflate(layoutInflater)
    }
}