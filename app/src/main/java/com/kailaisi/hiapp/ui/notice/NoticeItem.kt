package com.kailaisi.hiapp.ui.notice

import android.content.Intent
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.kailaisi.common.HiRoute
import com.kailaisi.common.util.DateUtil
import com.kailaisi.hi_ui.date_item.HiDataItem
import com.kailaisi.hi_ui.date_item.HiViewHolder
import com.kailaisi.hi_ui.icfont.IconFontTextView
import com.kailaisi.hiapp.R
import com.kailaisi.hiapp.model.NoticeInfo
import com.kailaisi.library.util.HiRes

class NoticeItem(val item: NoticeInfo) : HiDataItem<NoticeInfo, HiViewHolder>() {
    override fun onBindData(holder: HiViewHolder, position: Int) {
        holder.findViewById<TextView>(R.id.tv_title)?.text = item.title
        holder.findViewById<TextView>(R.id.tv_sub_title)?.text = item.subtitle
        holder.findViewById<TextView>(R.id.tv_pub_data)?.text = DateUtil.getMDData(item.createTime)
        holder.findViewById<IconFontTextView>(R.id.tv_icon)?.text =
            if ("goods" == item.type) HiRes.getString(R.string.if_notice_recommend) else HiRes.getString(
                R.string.if_notice_msg)
        holder.itemView.setOnClickListener {
            HiRoute.startActivity4Browser(item.url)
        }

    }

    override fun getItemLayoutRes(): Int {
        return R.layout.layout_notice_item
    }
}