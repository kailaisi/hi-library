package com.kailaisi.hiapp.demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.kailaisi.hiapp.R
import com.kailaisi.library.log.HiLog

class HiLogDemoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hi_log_demo)
        findViewById<Button>(R.id.button).setOnClickListener {
            printlog();
        }
    }

    private fun printlog() {
        HiLog.a(9900)
    }
}