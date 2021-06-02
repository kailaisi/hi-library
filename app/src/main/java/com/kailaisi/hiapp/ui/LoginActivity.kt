package com.kailaisi.hiapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kailaisi.common.ui.component.HiBaseActivity
import com.kailaisi.hiapp.R

class LoginActivity : HiBaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }
}