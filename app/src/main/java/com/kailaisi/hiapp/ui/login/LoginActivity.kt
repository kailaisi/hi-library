package com.kailaisi.hiapp.ui.login

import android.os.Bundle
import com.kailaisi.common.ui.component.HiBaseActivity
import com.kailaisi.hiapp.R
import com.kailaisi.hiapp.databinding.ActivityLoginBinding
import com.kailaisi.library.util.inflate

class LoginActivity : HiBaseActivity() {
    private val mBinding:ActivityLoginBinding by inflate()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }
}