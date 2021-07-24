package com.kailaisi.hiapp.ui.fragment.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kailaisi.common.HiRoute
import com.kailaisi.hi_ui.date_item.HiDataItem
import com.kailaisi.hi_ui.date_item.HiViewHolder
import com.kailaisi.hiapp.R
import com.kailaisi.hiapp.databinding.LayoutHomeOpGridItemBinding
import com.kailaisi.hiapp.model.Subcategory
import com.kailaisi.library.util.HiDisplayUtils
import com.kailaisi.library.util.HiRes

class GridItem(val list: List<Subcategory>) :
    HiDataItem<List<Subcategory>, HiViewHolder>(list) {
    override fun onBindData(holder: HiViewHolder, position: Int) {
        val context = holder.itemView.context
        val gridView = holder.itemView as RecyclerView
        gridView.adapter = GridAdapter(context, list)
    }

    override fun getItemView(parent: ViewGroup): View {
        val gridView = RecyclerView(parent.context)
        val params = RecyclerView.LayoutParams(
            RecyclerView.LayoutParams.MATCH_PARENT,
            RecyclerView.LayoutParams.WRAP_CONTENT
        )
        params.bottomMargin = HiDisplayUtils.dip2px(10f)
        gridView.layoutManager = GridLayoutManager(parent.context, 5)
        gridView.layoutParams = params
        gridView.setBackgroundColor(HiRes.getColor(R.color.color_white))
        return gridView
    }


    inner class GridAdapter(val context: Context, val list: List<Subcategory>) :
        RecyclerView.Adapter<GridAdapter.GridItemViewHolder>() {
        private var inflater = LayoutInflater.from(context)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridItemViewHolder {
            val binding = LayoutHomeOpGridItemBinding.inflate(inflater)
            return GridItemViewHolder(binding.root, binding)
        }

        override fun getItemCount(): Int {
            return list.size
        }

        override fun onBindViewHolder(holder: GridItemViewHolder, position: Int) {
            val subcategory = list[position]
            holder.binding.model = subcategory
            holder.itemView.setOnClickListener {
                //会跳转到子分类列表上面去，是一个单独的页面
                val bundle = Bundle()
                bundle.putString("categoryId", subcategory.categoryId)
                bundle.putString("subcategoryId", subcategory.subcategoryId)
                bundle.putString("categoryTitle", subcategory.subcategoryName)
                HiRoute.startActivity(context, bundle, "/goods/list")
            }
        }

        inner class GridItemViewHolder(view: View, var binding: LayoutHomeOpGridItemBinding) :
            HiViewHolder(view) {

        }
    }
}