package com.kailaisi.hiapp.ui.registor

import com.kailaisi.hiapp.BaseBindingActivity
import com.kailaisi.hiapp.databinding.ActivityRegisterBinding

class RegisterActivity : BaseBindingActivity<ActivityRegisterBinding>() {
    override fun initView() {
    }

    override fun getBinding(): ActivityRegisterBinding {
        return ActivityRegisterBinding.inflate(layoutInflater)
    }
}