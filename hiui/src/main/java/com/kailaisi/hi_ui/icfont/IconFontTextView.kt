package com.kailaisi.hi_ui.icfont

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatTextView

/**
 * 描述：用于支持全局iconfont资源的引用，可以在布局中直接设置text
 * <p/>作者：kailaisi
 * <br/>创建时间：2021-05-30:17:24
 */
class IconFontTextView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 1,
) : AppCompatTextView(context, attributeSet, defStyle) {
    init {
        gravity=Gravity.CENTER
        val createFromAsset = Typeface.createFromAsset(context.assets, "fonts/iconfont.ttf")
        typeface = createFromAsset
    }
}