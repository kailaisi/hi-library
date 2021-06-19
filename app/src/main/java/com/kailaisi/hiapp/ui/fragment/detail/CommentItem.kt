package com.kailaisi.hiapp.ui.fragment.detail

import com.kailaisi.hi_ui.date_item.HiDataItem
import com.kailaisi.hi_ui.date_item.HiViewHolder
import com.kailaisi.hiapp.R
import com.kailaisi.hiapp.model.CommentModel

/**
 * 描述：评论列表
 * <p/>作者：wu
 * <br/>创建时间：2021-06-19:22:57
 */
class CommentItem: HiDataItem<CommentModel, HiViewHolder>() {
    override fun onBindData(holder: HiViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemLayoutRes(): Int {
        return R.layout.layout_detail_item_comment
    }
}