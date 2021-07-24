package com.kailaisi.hiapp.demo.refresh

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.kailaisi.hi_ui.refresh.HiRefresh.HiRefreshListener
import com.kailaisi.hiapp.R
import kotlinx.android.synthetic.main.activity_refresh.*

class RefreshActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_refresh)
        val overView = HiLottieOverView(this)
        refresh.apply {
            setRefreshOverView(overView)
            setRefreshListener(object : HiRefreshListener {
                override fun onRefresh() {
                    Handler().postDelayed({
                        refresh.refreshFinished()
                    }, 1000)
                }

                override fun enableRefresh(): Boolean {
                    return true
                }
            })
        }
    }
}