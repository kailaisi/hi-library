package com.kailaisi.hiapp.ui.fragment.detail

import androidx.recyclerview.widget.RecyclerView
import com.kailaisi.hi_ui.banner.core.HiBannerMo
import com.kailaisi.hi_ui.date_item.HiDataItem
import com.kailaisi.hiapp.R
import com.kailaisi.hiapp.model.DetailModel
import com.kailaisi.hiapp.model.SliderImage

class HeaderItem(val sliderImage:List<SliderImage>?,val price:String,val numText:String,val goodsName:String): HiDataItem<DetailModel, RecyclerView.ViewHolder>() {
    override fun onBindData(holder: RecyclerView.ViewHolder, position: Int) {
        sliderImage?.map {
            // TODO: 2021/6/16
        }
        arrayListOf<HiBannerMo>()
    }

    override fun getItemLayoutRes(): Int {
        return R.layout.item_detail_header
    }
}