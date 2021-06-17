package com.kailaisi.hiapp.ui.fragment.detail

import com.kailaisi.hi_ui.date_item.HiDataItem
import com.kailaisi.hi_ui.date_item.HiViewHolder
import com.kailaisi.hiapp.model.GoodsModel

open class GoodsItem(val goodsModel: GoodsModel, val hotTab: Boolean) :
    HiDataItem<GoodsModel, HiViewHolder>(goodsModel) {
    private val MAX_TAG_SIZE = 3
    override fun onBindData(holder: HiViewHolder, position: Int) {
        TODO("Not yet implemented")
    }
}