package com.kailaisi.hiapp.demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.kailaisi.hiapp.R
import com.kailaisi.library.log.*

class HiLogDemoActivity : AppCompatActivity() {
    val viewPrinter by lazy { HiViewPrinter(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hi_log_demo)
        findViewById<Button>(R.id.button).setOnClickListener {
            printlog();
        }
        viewPrinter.printerProvider.showFloatingView()
    }

    private fun printlog() {
        HiLogManager.getInstance().addPrinter(viewPrinter)
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