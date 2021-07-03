package com.kailaisi.hi_ui.seach

import android.content.Context
import android.util.AttributeSet
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.kailaisi.hi_ui.icfont.IconFontTextView

/**
 * 描述：通用搜索框
 * <p/>作者：wu
 * <br/>创建时间：2021-07-03:22:36
 */
class HiSearchView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attributeSet, defStyleAttr) {
    private var editText: EditText? = null
    private var searchIcon: IconFontTextView? = null
    private var hintTv: TextView? = null
    private var searchIconContainer: LinearLayout? = null

    val viewAttrs=AttrsParse.parseSearchViewAttrs(context,attributeSet,defStyleAttr)

    init {
        initEditText()
        initClearIcon()
        initSearchIconHintContainer()
    }

    /**
     * 初始化右侧一键清除小按钮
     */
    private fun initClearIcon() {
        TODO("Not yet implemented")
    }

    /**
     * 初始化默认的提示语和搜索小图标
     */
    private fun initSearchIconHintContainer() {
        TODO("Not yet implemented")
    }

    /**
     * 初始化editText
     */
    private fun initEditText() {
        editText = EditText(context)
    }
}