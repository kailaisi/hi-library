package com.kailaisi.hiapp.demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.kailaisi.hiapp.R
import com.kailaisi.library.log.HiLog
import com.kailaisi.library.log.HiLogConfig
import com.kailaisi.library.log.HiLogType

class HiLogDemoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hi_log_demo)
        findViewById<Button>(R.id.button).setOnClickListener {
            printlog();
        }
    }

    private fun printlog() {
        HiLog.log(object : HiLogConfig() {
            override fun includeThread(): Boolean {
                return true
            }

            override fun stackTraceDepth(): Int {
                return 0
            }
        }, HiLogType.E, "---", "5556")
        HiLog.a("9900")
    }
}