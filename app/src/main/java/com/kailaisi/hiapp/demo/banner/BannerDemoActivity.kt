package com.kailaisi.hiapp.demo.banner

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.kailaisi.hi_ui.banner.indicator.HiNumberIndicator
import com.kailaisi.common.ui.component.BaseBindingActivity
import com.kailaisi.hiapp.R
import com.kailaisi.hiapp.databinding.ActivityBannerDemoBinding

class BannerDemoActivity : BaseBindingActivity<ActivityBannerDemoBinding>() {
    val list = listOf(
        "https://img1.baidu.com/it/u=1033706747,2169506273&fm=26&fmt=auto&gp=0.jpg",
        "https://img1.baidu.com/it/u=3013003396,2088685372&fm=26&fmt=auto&gp=0.jpg",
        "https://img1.baidu.com/it/u=542139767,750149024&fm=26&fmt=auto&gp=0.jpg",
        "https://img0.baidu.com/it/u=1895978868,2175253359&fm=26&fmt=auto&gp=0.jpg"
    )

    override fun initView() {
        init(true)
        Glide.with(this@BannerDemoActivity).load(list[0]).into(mBinding.ivImage)
        mBinding.sw.setOnCheckedChangeListener { buttonView, isChecked ->
            init(isChecked)
        }
    }

    fun init(auto: Boolean) {
        val molist = list.map {
            BannerMo().apply { url = it }
        }
        mBinding.banner.apply {
            setAutoPlay(auto)
            setIntervalTime(5000)
            setScrollDuration(3000)
            setHiIndicator(
                HiNumberIndicator(
                    context
                )
            )
            setBannerData(R.layout.item_banner_item, molist)
            setBindAdapter { holder, mo, _ ->
                val image = holder.findViewById<ImageView>(R.id.iv_image)
                Glide.with(this@BannerDemoActivity).load(mo.url).into(image)
            }
        }
    }

    override fun getBinding(): ActivityBannerDemoBinding {
        return ActivityBannerDemoBinding.inflate(layoutInflater)
    }
}