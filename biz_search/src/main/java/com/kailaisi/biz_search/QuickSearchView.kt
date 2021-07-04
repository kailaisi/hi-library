package com.kailaisi.biz_search

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kailaisi.hi_ui.date_item.HiAdapter
import com.kailaisi.hi_ui.date_item.HiDataItem
import com.kailaisi.hi_ui.date_item.HiViewHolder

/**
 * 描述：快搜的联想词列表
 * <p/>作者：wu
 * <br/>创建时间：2021-07-04:14:35
 */
class QuickSearchView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {
    init {
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter = HiAdapter(context)
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
    }

    fun bindData(list: List<KeyWord>, callback: (KeyWord) -> Unit) {
        val hiAdapter = adapter as HiAdapter
        hiAdapter.clearItems()
        hiAdapter.addItems(list.map { QuickSearchItem(it, callback) }, false)
    }

    private inner class QuickSearchItem(val keyWord: KeyWord, val callback: (KeyWord) -> Unit) :
        HiDataItem<KeyWord, HiViewHolder>() {
        override fun onBindData(holder: HiViewHolder, position: Int) {
            holder.findViewById<TextView>(R.id.tv)?.text = keyWord.keyWord
            holder.itemView.setOnClickListener {
                callback(keyWord)
            }
        }

        override fun getItemLayoutRes(): Int {
            return R.layout.layout_quick_search_list_item
        }

    }

}
