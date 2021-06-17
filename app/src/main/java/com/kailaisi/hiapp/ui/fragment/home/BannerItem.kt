package com.kailaisi.hiapp.ui.fragment.home

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ImageView
import androidx.core.os.TraceCompat
import androidx.recyclerview.widget.RecyclerView
import com.kailaisi.common.HiRoute
import com.kailaisi.common.ui.view.load
import com.kailaisi.hi_ui.banner.HiBanner
import com.kailaisi.hi_ui.banner.core.HiBannerMo
import com.kailaisi.hi_ui.date_item.HiDataItem
import com.kailaisi.hi_ui.date_item.HiViewHolder
import com.kailaisi.hiapp.R
import com.kailaisi.hiapp.model.HomeBanner
import com.kailaisi.library.util.HiDisplayUtils

class BannerItem(val list: List<HomeBanner>) :
    HiDataItem<List<HomeBanner>, HiViewHolder>(list) {
    override fun onBindData(holder: HiViewHolder, position: Int) {
        TraceCompat.beginSection("BannerItem")
        val context = holder.itemView.context
        val banner = holder.itemView as HiBanner

        banner.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                TraceCompat.endSection()
                banner.viewTreeObserver.removeOnPreDrawListener(this)
                return false
            }
        })

        val models = mutableListOf<HiBannerMo>()
        list.forEachIndexed { index, homeBanner ->
            val bannerMo = object : HiBannerMo() {}
            bannerMo.url = homeBanner.cover
            models.add(bannerMo)
        }
        banner.setOnBannerClickListener { viewHolder, bannerMo, position ->
            val homeBanner = list[position]
            if (TextUtils.equals(homeBanner.type, HomeBanner.TYPE_GOODS)) {
                val bundle = Bundle()
                bundle.putString("goodsId", list[position].id)
                HiRoute.startActivity(context, bundle, "/detail/main")
            } else {
                HiRoute.startActivity4Browser(homeBanner.url)
            }
        }
        banner.setBannerData(models)
        banner.setBindAdapter { viewHolder, mo, _ ->
            ((viewHolder.rootView) as ImageView).load(mo.url)
        }
    }

    override fun getItemView(parent: ViewGroup): View? {
        val context = parent.context
        val banner = HiBanner(context)
        val params = RecyclerView.LayoutParams(
            RecyclerView.LayoutParams.MATCH_PARENT,
            HiDisplayUtils.dip2px(160f)
        )
        params.bottomMargin = HiDisplayUtils.dip2px(10f)
        banner.layoutParams = params
        banner.setBackgroundColor(context.resources.getColor(R.color.color_white))
        return banner
    }

    override fun onViewAttachedToWindow(holder: HiViewHolder) {
        super.onViewAttachedToWindow(holder)
        val itemView = holder.itemView
        val layoutParams = itemView.layoutParams
        layoutParams.height = (HiDisplayUtils.getScreenWidth()/ 2.6).toInt()
        itemView.layoutParams = layoutParams
    }
}