package com.kailaisi.hiapp.ui.fragment.detail

import android.text.SpannableString
import android.text.Spanned
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.text.buildSpannedString
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kailaisi.common.ui.view.load
import com.kailaisi.hi_ui.date_item.HiAdapter
import com.kailaisi.hi_ui.date_item.HiDataItem
import com.kailaisi.hi_ui.date_item.HiViewHolder
import com.kailaisi.hiapp.R
import com.kailaisi.hiapp.model.DetailModel
import com.kailaisi.hiapp.model.Shop
import com.kailaisi.pub_mod.GoodsItem
import com.kailaisi.library.util.HiRes
import com.kailaisi.pub_mod.GoodsModel

/**
 * 描述：店铺信息的展示页
 * 作者：kailaisii
 * 创建时间：2021/6/21 5:33 PM
 */
class ShopItem(val detailModel: DetailModel) : HiDataItem<Shop, HiViewHolder>() {
    val SHOP_GOODS_ITEM_SPAN_COUNT = 3
    override fun onBindData(holder: HiViewHolder, position: Int) {
        val context = holder.itemView.context ?: return
        detailModel.shop?.let {
            holder.findViewById<TextView>(R.id.shop_title)?.text = it.name
            holder.findViewById<ImageView>(R.id.shop_logo)?.load(it.logo)
            holder.findViewById<TextView>(R.id.shop_desc)?.text =
                String.format(context.getString(R.string.shop_desc_pre),
                    it.goodsNum,
                    it.completedNum)

            it.evaluation?.let {
                val content = holder.findViewById<LinearLayout>(R.id.ll_content)!!
                content.visibility = View.VISIBLE
                var index = 0
                val serviceTags = it.split(" ")
                for (tagIndex in 0..serviceTags.size / 2) {

                    val tagView = if (tagIndex < content.childCount) {
                        content.getChildAt(tagIndex) as TextView
                    } else {
                        TextView(context)
                    }
                    val layoutParams = LinearLayout.LayoutParams(0, WRAP_CONTENT)
                    layoutParams.weight = 1f
                    tagView.layoutParams = layoutParams
                    tagView.textSize = 14f
                    tagView.gravity = Gravity.CENTER
                    tagView.setTextColor(HiRes.getColor(R.color.color_666))

                    val serviceName = serviceTags[index]
                    val serviceTag = serviceTags[index + 1]
                    index += 2

                    val spanText = spanServiceTag(serviceName, serviceTag)
                    tagView.text = spanText
                    content.addView(tagView)
                }
            }

            detailModel.flowGoods?.let {
                val flowRecyclerView = holder.findViewById<RecyclerView>(R.id.rv_item)!!
                flowRecyclerView.visibility = View.VISIBLE
                if (flowRecyclerView.layoutManager == null) {
                    flowRecyclerView.layoutManager =
                        GridLayoutManager(context, SHOP_GOODS_ITEM_SPAN_COUNT)
                }
                if (flowRecyclerView.adapter == null) {
                    flowRecyclerView.adapter = HiAdapter(context)
                }
                val map = it.map {
                    ShopGoodsItem(it)
                }
                val hiAdapter = flowRecyclerView.adapter as HiAdapter
                hiAdapter.clearItems()
                hiAdapter.addItems(map, true)
            }
        }

        holder.findViewById<TextView>(R.id.shop_logo)?.text = detailModel.goodsName
    }

    private inner class ShopGoodsItem(goodsModel: GoodsModel) : GoodsItem(goodsModel, false) {
        override fun getItemLayoutRes(): Int {
            return R.layout.layout_detail_goods_list_item
        }

        override fun onViewAttachedToWindow(holder: GoodsItemHolder) {
            super.onViewAttachedToWindow(holder)
            val viewGroup = holder.itemView.parent as ViewGroup
            val availableWidth =
                viewGroup.measuredWidth - viewGroup.paddingLeft - viewGroup.paddingRight
            val itemWidth = availableWidth / SHOP_GOODS_ITEM_SPAN_COUNT
            val itemImage = holder.findViewById<ImageView>(R.id.shop_logo)!!
            val layoutParams = itemImage.layoutParams
            layoutParams.width = itemWidth
            layoutParams.height = itemWidth
            itemImage.layoutParams = layoutParams
        }
    }


    private fun spanServiceTag(
        serviceName: String,
        serviceTag: String,
    ): CharSequence {
        return buildSpannedString {
            val ss = SpannableString(serviceTag)
            ss.setSpan(ForegroundColorSpan(HiRes.getColor(R.color.color_c61)),
                0,
                ss.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            ss.setSpan(BackgroundColorSpan(HiRes.getColor(R.color.color_f8e)),
                0,
                ss.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            append(serviceName)
            append(serviceTag)
        }
    }

    override fun getItemLayoutRes(): Int {
        return R.layout.layout_shop_item
    }
}