package com.kailaisi.common.ui.view

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.StringRes
import com.kailaisi.common.R

/**
 *
 */
class EmptyView @JvmOverloads constructor(
    context: Context,
    attributes: AttributeSet? = null,
    defstyle: Int = 0,
) : LinearLayout(context, attributes, defstyle) {
    private var title: TextView
    private var icon: TextView
    private var desc: TextView
    private var button: Button

    init {

        orientation = VERTICAL
        gravity = Gravity.CENTER
        LayoutInflater.from(context).inflate(R.layout.layout_empty_view, this, true)
        icon = findViewById(R.id.empty_icon)
        title = findViewById(R.id.empty_title)
        desc = findViewById(R.id.empty_text)
        button = findViewById(R.id.empty_action)


    }

    /**
     * 设置icon，需要在string.xml中定义 iconfont.ttf中的unicode码
     */
    fun setIcon(@StringRes iconRes: Int) {
        icon.setText(iconRes)
    }

    fun setTitle(text: String) {
        title.text = text
        title.visibility = if (TextUtils.isEmpty(text)) GONE else VISIBLE
    }

    fun setDesc(text: String) {
        desc.text = text
        desc.visibility = if (TextUtils.isEmpty(text)) GONE else VISIBLE
    }


    @JvmOverloads
    fun setHelpAction(@StringRes actionId: Int = R.string.if_detail, listener: OnClickListener) {
        desc.setText(actionId)
        desc.setOnClickListener(listener)
        desc.visibility = if (actionId == -1) GONE else VISIBLE
    }


    fun setButton(text: String, listener: OnClickListener) {
        if (TextUtils.isEmpty(text)) {
            button.visibility = GONE
        } else {
            button.visibility = VISIBLE
            button.text = text
            button.setOnClickListener(listener)
        }
    }
}