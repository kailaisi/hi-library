package com.kailaisi.biz_search

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import com.kailaisi.hi_ui.date_item.HiAdapter
import com.kailaisi.hi_ui.recyclerview.HiRecyclerView
import com.kailaisi.pub_mod.GoodsItem
import com.kailaisi.pub_mod.GoodsModel

/**
 * 描述：快搜的联想词列表
 * <p/>作者：wu
 * <br/>创建时间：2021-07-04:14:35
 */
class GoodsSearchView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : HiRecyclerView(context, attrs, defStyleAttr) {
    init {
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter = HiAdapter(context)
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        R.layout.layout_quick_search_list_item
    }

    fun bindData(
        list: List<GoodsModel>,
        loadInit: Boolean
    ) {
        val hiAdapter = adapter as HiAdapter
        if (loadInit) {
            hiAdapter.clearItems()
        }
        hiAdapter.addItems(list.map { GoodsItem(it,true) }, false)
    }
}
