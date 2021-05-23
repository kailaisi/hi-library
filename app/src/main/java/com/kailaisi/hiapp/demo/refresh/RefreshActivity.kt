package com.kailaisi.hiapp.demo.refresh

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.kailaisi.hi_ui.refresh.HiRefresh
import com.kailaisi.hi_ui.refresh.HiRefresh.HiRefreshListener
import com.kailaisi.hi_ui.refresh.HiRefreshLayout
import com.kailaisi.hi_ui.refresh.HiTextView
import com.kailaisi.hiapp.databinding.ActivityRefreshBinding

class RefreshActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityRefreshBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val overView = HiLottieOverView(this)
        binding.refresh.apply {
            setRefreshOverView(overView)
            setRefreshListener(object : HiRefreshListener {
                override fun onRefresh() {
                    Handler().postDelayed({
                        binding.refresh.refreshFinished()
                    }, 1000)
                }

                override fun enableRefresh(): Boolean {
                    return true
                }
            })
        }
    }
}