package com.kailaisi.biz_search

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.layout_history_search.view.*

/**
 * 描述：历史搜索列表
 * <p/>作者：wu
 * <br/>创建时间：2021-07-05:21:47
 */
class HistorySearchView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private val keyWord = ArrayList<KeyWord>()

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_history_search, this, true)
        orientation = VERTICAL

    }

    fun bindData(history: List<KeyWord>) {
        if (history.isNullOrEmpty()) return
        keyWord.clear()
        keyWord.addAll(history)
        history.forEachIndexed { index, keyWord ->
            val childCount = chip_group.childCount
            //复用
            val chipItem: Chip
            if (index < childCount) {
                chipItem = chip_group.getChildAt(index) as Chip
            } else {
                chipItem = generateChip()
                chip_group.addView(chipItem)
            }
            chipItem.text = history[index].keyWord
        }
    }

    private fun generateChip(): Chip {
        val chip: Chip =
            LayoutInflater.from(context)
                .inflate(R.layout.layout_history_chip_item, chip_group, false) as Chip
        chip.isClickable = true
        chip.isChecked = false
        chip.id = chip_group.childCount
        return chip
    }

    fun setOnCheckedChangedListener(callback: (KeyWord) -> Unit) {
        chip_group.setOnCheckedChangeListener { group, checkedId ->
            for (index in 0 until chip_group.childCount) {
                if (chip_group.getChildAt(index).id == checkedId) {
                    callback(keyWord[index])
                    break
                }
            }
        }
    }

    fun setOnHistoryClearListener(callback: () -> Unit) {
        chip_group.removeAllViews()
        keyWord.clear()
        callback()
    }
}
