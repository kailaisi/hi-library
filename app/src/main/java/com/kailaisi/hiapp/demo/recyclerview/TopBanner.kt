package com.kailaisi.hiapp.demo.recyclerview

import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.kailaisi.hi_ui.date_item.HiDataItem
import com.kailaisi.hiapp.R

/**
 * 描述：
 * <p/>作者：wu
 * <br/>创建时间：2021-05-27:22:54
 */
class TopBanner(data:ItemData):HiDataItem<ItemData,RecyclerView.ViewHolder>(data) {
    override fun onBindData(holder: RecyclerView.ViewHolder, position: Int) {
        var findViewById = holder.itemView.findViewById<ImageView>(R.id.iv_image)
        findViewById.setImageResource(R.mipmap.ic_launcher)
    }
    override fun getItemLayoutRes(): Int {
        return R.layout.layout_item_banner
    }
}

class ItemData {

}
