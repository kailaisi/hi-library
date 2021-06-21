package com.kailaisi.hiapp.ui.fragment.detail

import android.content.res.ColorStateList
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.get
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.shape.ShapeAppearanceModel
import com.kailaisi.common.ui.view.loadCircle
import com.kailaisi.hi_ui.date_item.HiDataItem
import com.kailaisi.hi_ui.date_item.HiViewHolder
import com.kailaisi.hiapp.R
import com.kailaisi.hiapp.model.CommentModel
import com.kailaisi.hiapp.model.DetailModel
import com.kailaisi.library.util.HiDisplayUtils
import com.kailaisi.library.util.HiRes
import kotlin.math.min

/**
 * 描述：评论列表
 * <p/>作者：wu
 * <br/>创建时间：2021-06-19:22:57
 */
class CommentItem(val model: DetailModel) : HiDataItem<CommentModel, HiViewHolder>() {
    override fun onBindData(holder: HiViewHolder, position: Int) {
        val context = holder.itemView.context ?: return
        holder.findViewById<TextView>(R.id.tv_title)?.text = model.commentCountTitle
        val commentTags = model.commentTags
        commentTags.let {
            it.split(" ").forEachIndexed { index, s ->
                val chipGroup = holder.findViewById<ChipGroup>(R.id.chip_group)!!
                val chip = if (index < chipGroup.childCount) {
                    chipGroup.getChildAt(index) as Chip
                } else {
                    /*这里会存在复用的问额*/
                    val chip = Chip(context)
                    chip.shapeAppearanceModel =
                        ShapeAppearanceModel().withCornerSize(HiDisplayUtils.dip2px(4f).toFloat())
                    chip.chipBackgroundColor =
                        ColorStateList.valueOf(HiRes.getColor(R.color.color_faf0))
                    chip.textSize = 14f
                    chip.setTextColor(HiRes.getColor(R.color.color_999))
                    chip.isChipIconVisible = false
                    chip.gravity = Gravity.CENTER
                    chip.isCheckable = false
                    chipGroup.addView(chip)
                    chip
                }
                chip.text = s
            }
        }

        model.commentModels?.let {
            val commentRoot: LinearLayout = holder.findViewById(R.id.ll_comment)!!
            for (index in 0..min(it.size - 1, 3)) {
                val commentModel = it[index]
                var itemView = if (index<commentRoot.childCount){
                    /*处理复用问题*/
                    commentRoot[index]
                }else {
                    LayoutInflater.from(context).inflate(R.layout.layout_detail_item_comment_item, null, false)
                }
                itemView.findViewById<ImageView>(R.id.iv_avatar).loadCircle(commentModel.avatar)
                itemView.findViewById<TextView>(R.id.tv_user_name).text=commentModel.nickName
                itemView.findViewById<TextView>(R.id.tv_comment).text=commentModel.content
            }
        }
    }

    override fun getItemLayoutRes(): Int {
        return R.layout.layout_detail_item_comment
    }
}