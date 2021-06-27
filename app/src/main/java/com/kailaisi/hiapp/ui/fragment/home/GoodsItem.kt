package com.kailaisi.hiapp.ui.fragment.home

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.kailaisi.common.HiRoute
import com.kailaisi.common.ui.view.load
import com.kailaisi.hi_ui.date_item.HiDataItem
import com.kailaisi.hi_ui.date_item.HiViewHolder
import com.kailaisi.hiapp.R
import com.kailaisi.hiapp.model.GoodsModel
import com.kailaisi.hiapp.model.selectPrice
import com.kailaisi.library.util.HiDisplayUtils

open class GoodsItem(val goodsModel: GoodsModel, val hotTab: Boolean = false) :
    HiDataItem<GoodsModel, HiViewHolder>(goodsModel) {
    protected var binding: ViewDataBinding? = null
    private val MAX_TAG_SIZE = 3
    override fun onBindData(holder: HiViewHolder, position: Int) {
        val context = holder.itemView.context
        holder.findViewById<ImageView>(R.id.item_image)?.load(goodsModel.sliderImage)
        holder.findViewById<TextView>(R.id.item_title)?.text = goodsModel.goodsName
        holder.findViewById<TextView>(R.id.item_price)?.text =
            selectPrice(goodsModel.groupPrice, goodsModel.marketPrice)
        holder.findViewById<TextView>(R.id.item_sale_desc)?.text = goodsModel.completedNumText

        //标签
        val itemLabelContainer = holder.findViewById<LinearLayout>(R.id.item_label_container)
        if (itemLabelContainer != null) {
            if (!TextUtils.isEmpty(goodsModel.tags)) {
                itemLabelContainer.visibility = View.VISIBLE
                val split = goodsModel.tags!!.split(" ")
                for (index in split.indices) { //0...split.size-1
                    //0  ---3
                    val childCount = itemLabelContainer.childCount
                    if (index > MAX_TAG_SIZE - 1) {
                        //倒叙
                        for (index in childCount - 1 downTo MAX_TAG_SIZE - 1) {
                            // itemLabelContainer childcount =5
                            // 3，后面的两个都需要被删除
                            itemLabelContainer.removeViewAt(index)
                        }
                        break
                    }
                    //这里有个问题，有着一个复用的问题   5 ,4
                    //解决上下滑动复用的问题--重复创建的问题
                    val labelView: TextView = if (index > childCount - 1) {
                        val view = createLabelView(context, index != 0)
                        itemLabelContainer.addView(view)
                        view
                    } else {
                        itemLabelContainer.getChildAt(index) as TextView
                    }
                    labelView.text = split[index]
                }
            } else {
                itemLabelContainer.visibility = View.GONE
            }
        }

        if (!hotTab) {
            val margin = HiDisplayUtils.dip2px(2f)
            /*动态的根据位于左右位置进行布局的设置*/
            val params = holder.itemView.layoutParams as RecyclerView.LayoutParams
            val parentLeft = hiAdapter?.getAttachRecyclerView()?.left ?: 0
            val parentPaddingLeft = hiAdapter?.getAttachRecyclerView()?.paddingLeft ?: 0
            val itemLeft = holder.itemView.left
            if (itemLeft == (parentLeft + parentPaddingLeft)) {
                params.rightMargin = margin
            } else {
                params.leftMargin = margin
            }
            holder.itemView.layoutParams = params
        }

        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("goodsId", goodsModel.goodsId)
            bundle.putParcelable("goodsModel", goodsModel)
            HiRoute.startActivity(context, bundle, HiRoute.Destination.GOODS_DETAIL.path)
        }
    }

    override fun getItemView(parent: ViewGroup): View? {
        val from = LayoutInflater.from(parent.context)
        binding = DataBindingUtil.inflate<ViewDataBinding>(from, getItemLayoutRes(), parent, false)
        return binding!!.root
    }

    private fun createLabelView(context: Context, withLeftMargin: Boolean): TextView {
        val labelView = TextView(context)
        labelView.setTextColor(ContextCompat.getColor(context, R.color.color_e75))
        labelView.setBackgroundResource(R.drawable.shape_goods_label)
        labelView.textSize = 11f
        labelView.gravity = Gravity.CENTER
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            HiDisplayUtils.dip2px(16f)
        )
        params.leftMargin = if (withLeftMargin) HiDisplayUtils.dip2px(5f) else 0
        labelView.layoutParams = params
        return labelView
    }

    override fun getItemLayoutRes(): Int {
        return if (hotTab) R.layout.layout_home_goods_list_item1 else R.layout.layout_home_goods_list_item2
    }

    override fun getSpanSize(): Int {
        return if (hotTab) super.getSpanSize() else 1
    }
}