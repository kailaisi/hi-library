package com.kailaisi.hiapp.ui.fragment.detail

import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.style.AbsoluteSizeSpan
import android.widget.ImageView
import android.widget.TextView
import com.kailaisi.common.ui.view.load
import com.kailaisi.hi_ui.banner.HiBanner
import com.kailaisi.hi_ui.banner.core.HiBannerMo
import com.kailaisi.hi_ui.banner.indicator.HiNumberIndicator
import com.kailaisi.hi_ui.date_item.HiDataItem
import com.kailaisi.hi_ui.date_item.HiViewHolder
import com.kailaisi.hiapp.R
import com.kailaisi.hiapp.model.DetailModel
import com.kailaisi.hiapp.model.SliderImage

class HeaderItem(
    val sliderImage: List<SliderImage>?,
    val price: String?,
    val numText: String?,
    val goodsName: String?
) : HiDataItem<DetailModel, HiViewHolder>() {
    override fun onBindData(viewHolder: HiViewHolder, position: Int) {
        val context = viewHolder.view.context ?: return
        val map = sliderImage?.map {
            object : HiBannerMo() {}.apply {
                url = it.url
            }
        } ?: emptyList()
        val hiBanner = viewHolder.findViewById<HiBanner>(R.id.hi_banner)?.apply {
            setHiIndicator(HiNumberIndicator(context))
            setBannerData(map)
            setBindAdapter { holder, mo, pos ->
                val imageView = holder.rootView as ImageView
                mo?.url?.let { imageView.load(it) }
            }
        }
        viewHolder.findViewById<TextView>(R.id.price)?.text = price
        viewHolder.findViewById<TextView>(R.id.sale_desc)?.text=numText
        viewHolder.findViewById<TextView>(R.id.titile)?.text=goodsName
    }

    private fun spanPrice(price: String?): CharSequence {
        if (TextUtils.isEmpty(price)) return ""
        val ss = SpannableString(price)
        ss.setSpan(AbsoluteSizeSpan(18, true), 1, ss.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        return ss
    }

    override fun getItemLayoutRes(): Int {
        return R.layout.item_detail_header
    }
}