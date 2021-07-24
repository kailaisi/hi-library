package com.kailaisi.hiapp.ui.fragment.category

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.kailaisi.hiapp.R
import com.kailaisi.library.util.HiDisplayUtils
import com.kailaisi.library.util.HiRes

/*
* 通过Decortion实现表头功能
*/
class CategoryItemDecoration(val callback: (Int) -> String, val spanCount: Int = 1) :
    RecyclerView.ItemDecoration() {
    private val groupFirstPosition = mutableMapOf<String, Int>()

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        paint.style = Paint.Style.STROKE
        paint.color = HiRes.getColor(R.color.black)
        paint.textSize = HiDisplayUtils.dip2px(14f).toFloat()
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {
        /*根据view找到他在列表中的位置*/
        val adapterPosition = parent.getChildAdapterPosition(view)
        if (adapterPosition >= parent.adapter!!.itemCount || adapterPosition < 0) {
            return
        }
        //拿到当前position对应的groupName
        val groupName = callback(adapterPosition)
        //拿到前面 groupName
        val preGroupName = if (adapterPosition > 0) callback(adapterPosition - 1) else null
        val same = groupName == preGroupName
        if (!same && !groupFirstPosition.containsKey(groupName)) {
            //当前的postion是当前组的第一个位置，需要存储下来，然后在后面计算，计算后面的item是否是第一行
            groupFirstPosition[groupName] = adapterPosition
        }
        /*找到当前groupName对应的第一个数据位置*/
        val firstRowPosition = groupFirstPosition[groupName] ?: 0
        /*判断当前postion是否和第一个表头位置在同一行*/
        val sameRow = adapterPosition - firstRowPosition in 0..spanCount
        if (!same) {
            outRect.set(0, HiDisplayUtils.dip2px(40f), 0, 0)
        }
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val childCount = parent.childCount
        for (index in 0 until childCount) {
            val view = parent.getChildAt(index)
            val childAdapterPosition = parent.getChildAdapterPosition(view)
            if (childAdapterPosition < 0 || childAdapterPosition >= parent.adapter!!.itemCount) {
                return
            }
            val groupName = callback(childAdapterPosition)
            //判断当前是否是分组第一个位置
            val groupFirstPos = groupFirstPosition[groupName]
            if (groupFirstPos == childAdapterPosition) {
                val decoration = Rect()
                /*拿到对应的位置信息*/
                parent.getDecoratedBoundsWithMargins(view, decoration)
                val textBounds = Rect()
                /*获取文字的信息*/
                paint.getTextBounds(groupName, 0, groupName.length, textBounds)

                c.drawText(groupName,
                    HiDisplayUtils.dip2px(16f).toFloat(),
                    decoration.top + 2 * textBounds.height().toFloat(),
                    paint)
            }
        }
    }

    fun clear() {
        groupFirstPosition.clear()
    }
}