package com.kailaisi.hi_ui.seach

import android.content.Context
import android.graphics.Color
import android.text.InputFilter
import android.text.TextUtils
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import com.kailaisi.hi_ui.R
import com.kailaisi.hi_ui.icfont.IconFontTextView
import com.kailaisi.library.util.MainHandler
import java.lang.IllegalArgumentException

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
    companion object {
        const val LEFT = 1
        const val CENTER = 2
        const val DEBOUNCE_DURATION = 2000L
    }

    /**
     * 数据变化的监听器
     */
    private var textListener: ((String?) -> Unit)? = null

    //关键词部分
    private var keywordContainer: LinearLayout? = null
    private var keywordTv: TextView? = null
    private var kwClearIcon: IconFontTextView? = null

    //搜索部分相关控件
    private var clearIcon: IconFontTextView? = null
    var editText: EditText? = null

    //搜索图标
    private var searchIcon: IconFontTextView? = null
    private var hintTv: TextView? = null
    private var searchIconContainer: LinearLayout? = null

    val viewAttrs = AttrsParse.parseSearchViewAttrs(context, attributeSet, defStyleAttr)

    init {
        //初始化一些信息
        initEditText()
        initClearIcon()
        initSearchIconHintContainer()
        background = viewAttrs.searchBackground
        editText?.doAfterTextChanged {
            val hasCount = it?.length ?: 0 > 0
            clearIcon?.visibility = if (hasCount) View.VISIBLE else View.GONE
            searchIconContainer?.visibility = if (hasCount) View.GONE else View.VISIBLE
            if (textListener != null) {
                MainHandler.remove(debounceRunnable)
                MainHandler.postDelay(DEBOUNCE_DURATION, debounceRunnable)
            }
        }
    }

    private val debounceRunnable = Runnable {
        textListener?.invoke(editText?.text.toString())
    }

    fun setDebounceTextChangeListener(afterTextChangedListener: ((String?) -> Unit)?) {
        textListener = afterTextChangedListener
    }

    fun setKeyWord(keyword: String?, listener: OnClickListener) {
        //当用户点击 联想词面板的时候，会调用该方法，把关键词设置到搜索上面
        ensureKeywordContainer()
        //显示出来keyword
        toggleSearchViewsVisibility(true)
        editText?.text = null
        keywordTv?.text = keyword
        kwClearIcon?.setOnClickListener {
            //点击了关键词清除键，则回复默认提示语的显示
            toggleSearchViewsVisibility(false)
            listener.onClick(it)
        }
    }

    fun setClearIconClickListener(listener: OnClickListener?) {
        clearIcon?.setOnClickListener {
            editText?.text = null
            searchIconContainer?.visibility = View.VISIBLE
            hintTv?.visibility = View.VISIBLE
            listener?.onClick(it)
        }
    }

    fun setHintText(hintText: String) {
        hintTv?.text = hintText
    }

    private fun toggleSearchViewsVisibility(showKeyword: Boolean) {
        editText?.visibility = if (showKeyword) View.GONE else View.VISIBLE
        clearIcon?.visibility = GONE
        searchIconContainer?.visibility = if (showKeyword) View.GONE else View.VISIBLE
        searchIcon?.visibility = if (showKeyword) View.GONE else View.VISIBLE
        hintTv?.visibility = if (showKeyword) View.GONE else View.VISIBLE

        keywordContainer?.visibility = if (showKeyword) View.VISIBLE else View.GONE
    }

    private fun ensureKeywordContainer() {
        if (keywordContainer == null) {
            keywordContainer = LinearLayout(context)
            if (!TextUtils.isEmpty(viewAttrs.keywordClearIcon)) {
                kwClearIcon = IconFontTextView(context, null).apply {
                    setTextColor(viewAttrs.keywordColor)
                    setTextSize(TypedValue.COMPLEX_UNIT_PX, viewAttrs.keywordClearIconSize)
                    text = viewAttrs.keywordClearIcon
                    id = R.id.id_search_keyword_clear_icon
                    setPadding(
                        viewAttrs.iconPadding,
                        viewAttrs.iconPadding / 2,
                        viewAttrs.iconPadding,
                        viewAttrs.iconPadding / 2
                    )
                }
            }
            keywordTv = TextView(context).apply {
                setTextSize(TypedValue.COMPLEX_UNIT_PX, viewAttrs.keywordSize)
                setTextColor(viewAttrs.keywordColor)
                //消除TextView的默认留白
                includeFontPadding = false
                isSingleLine = true
                ellipsize = TextUtils.TruncateAt.END
                filters = arrayOf(InputFilter.LengthFilter(viewAttrs.keywordMaxLen))
                id = R.id.id_search_keyword_text_view
                setPadding(
                    viewAttrs.iconPadding,
                    viewAttrs.iconPadding / 2,
                    0,
                    viewAttrs.iconPadding / 2
                )
            }

            keywordContainer = LinearLayout(context).apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER
                background = viewAttrs.keywordBackground
            }
            keywordContainer?.addView(
                keywordTv,
                LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    (LinearLayout.LayoutParams.WRAP_CONTENT)
                )
            )
            kwClearIcon?.let {
                keywordContainer?.addView(
                    it, LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        (LinearLayout.LayoutParams.WRAP_CONTENT)
                    )
                )
            }
            val params = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            params.addRule(CENTER_VERTICAL)
            params.addRule(ALIGN_PARENT_LEFT)
            params.leftMargin = viewAttrs.iconPadding
            params.rightMargin = viewAttrs.iconPadding
            addView(keywordContainer, params)
        }
    }

    /**
     * 初始化右侧一键清除小按钮
     */
    private fun initClearIcon() {
        if (TextUtils.isEmpty(viewAttrs.clearIcon)) return
        clearIcon = IconFontTextView(context, null).apply {
            setPadding(
                viewAttrs.iconPadding,
                viewAttrs.iconPadding,
                viewAttrs.iconPadding,
                viewAttrs.iconPadding
            )
            text = viewAttrs.clearIcon
            setTextColor(viewAttrs.searchTextColor)
            setTextSize(TypedValue.COMPLEX_UNIT_PX, viewAttrs.clearIconSize)
        }
        val params = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        params.addRule(CENTER_VERTICAL)
        params.addRule(ALIGN_PARENT_RIGHT)
        //默认隐藏
        clearIcon?.visibility = View.GONE
        setClearIconClickListener(null)
        addView(clearIcon, params)
    }

    /**
     * 初始化默认的提示语和搜索小图标
     */
    private fun initSearchIconHintContainer() {
        //hint view
        hintTv = TextView(context).apply {
            setTextColor(viewAttrs.hintTextColor)
            setTextSize(TypedValue.COMPLEX_UNIT_PX, viewAttrs.hintTextSize)
            isSingleLine = true
            text = viewAttrs.hintText
            id = R.id.id_search_hint_view
        }

        //search icon
        searchIcon = IconFontTextView(context, null).apply {
            setTextSize(TypedValue.COMPLEX_UNIT_PX, viewAttrs.searchIconSize)
            setTextColor(viewAttrs.hintTextColor)
            text = viewAttrs.searchIcon
            id = R.id.id_search_icon
            setPadding(viewAttrs.iconPadding, 0, viewAttrs.iconPadding / 2, 0)
        }

        //container
        searchIconContainer = LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER
        }
        searchIconContainer?.addView(searchIcon)
        searchIconContainer?.addView(hintTv)
        val params = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        params.addRule(CENTER_VERTICAL)
        when (viewAttrs.gravity) {
            CENTER -> params.addRule(CENTER_IN_PARENT)
            LEFT -> params.addRule(ALIGN_PARENT_LEFT)
            else -> throw IllegalArgumentException("not support gravity for now.")
        }
        addView(searchIconContainer, params)
    }

    /**
     * 初始化editText
     */
    private fun initEditText() {
        editText = EditText(context).apply {
            setTextColor(viewAttrs.searchTextColor)
            isSingleLine = true
            setBackgroundColor(Color.TRANSPARENT)
            setTextSize(TypedValue.COMPLEX_UNIT_PX, viewAttrs.searchTextSize)
            setPadding(viewAttrs.iconPadding, 0, viewAttrs.iconPadding, 0)
            id = R.id.id_search_edit_view
        }
        val params = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        params.addRule(CENTER_VERTICAL)
        addView(editText, params)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        //防止内存泄漏
        MainHandler.remove(debounceRunnable)
    }

    fun getKeyword(): String {
        return keywordTv?.text.toString()
    }
}
