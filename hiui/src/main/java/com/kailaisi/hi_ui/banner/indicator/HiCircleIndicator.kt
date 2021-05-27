package com.kailaisi.hi_ui.banner.indicator

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import com.kailaisi.hi_ui.R
import com.kailaisi.hi_ui.banner.core.HiIndicator
import com.kailaisi.library.util.HiDisplayUtils

/**
 * 默认的圆形指示器
 */
class HiCircleIndicator @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), HiIndicator<FrameLayout?> {
    private var leftRightPadding = 0
    private var topBottomPadding = 0

    @DrawableRes
    private val pointNormal = R.drawable.shap_point_normal

    @DrawableRes
    private val pointSelected = R.drawable.shap_point_selected
    private fun init() {
        leftRightPadding = HiDisplayUtils.dip2px(context, 5f)
        topBottomPadding = HiDisplayUtils.dip2px(context, 15f)
    }

    override fun get(): FrameLayout {
        return this
    }

    override fun onInflate(count: Int) {
        removeAllViews()
        if (count <= 0) {
            return
        }
        val group = LinearLayout(context)
        group.orientation = LinearLayout.HORIZONTAL
        val params = LinearLayout.LayoutParams(VWC, VWC)
        params.gravity = Gravity.CENTER_VERTICAL
        params.setMargins(leftRightPadding, topBottomPadding, leftRightPadding, topBottomPadding)
        for (i in 0 until count) {
            val imageView = ImageView(context)
            imageView.layoutParams = params
            if (i == 0) {
                imageView.setImageResource(pointSelected)
            } else {
                imageView.setImageResource(pointNormal)
            }
            group.addView(imageView)
        }
        val groupParam = LayoutParams(VWC, VWC)
        groupParam.gravity = Gravity.CENTER or Gravity.BOTTOM
        addView(group, groupParam)
    }

    override fun onPointChange(current: Int, count: Int) {
        val viewGroup = getChildAt(0) as ViewGroup
        for (i in 0 until viewGroup.childCount) {
            val child = viewGroup.getChildAt(i) as ImageView
            if (i == current) {
                child.setImageResource(pointSelected)
            } else {
                child.setImageResource(pointNormal)
            }
            child.requestLayout()
        }
    }

    companion object {
        const val VWC = ViewGroup.LayoutParams.WRAP_CONTENT
    }

    init {
        init()
    }
}