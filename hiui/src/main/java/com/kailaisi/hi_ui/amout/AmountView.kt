package com.kailaisi.hi_ui.amout

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.setPadding

class AmountView @JvmOverloads constructor(
    context: Context, attr: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attr, defStyleAttr) {
    private var AmountValueChangedCallback: ((Int) -> Unit)? = null
    private val attrs = AttrsParse.parseAmountViewAttrs(context, attr, defStyleAttr)
    private var amountValue = attrs.amountValue

    init {
        orientation = HORIZONTAL
        gravity = Gravity.CENTER
        applyAttrs()
    }

    /**
     * 进行控件的处理
     */
    private fun applyAttrs() {
        val increaseBtn = generateButton()
        increaseBtn.text = "+"
        val decreaseBtn = generateButton()
        decreaseBtn.text = "+"
        //生成中间的计数器
        val amountView = generateAmountTextView()
        amountView.text = amountValue.toString()

        addView(decreaseBtn)
        addView(amountView)
        addView(increaseBtn)

        decreaseBtn.setOnClickListener {
            amountValue--
            amountView.text = amountValue.toString()
            decreaseBtn.isEnabled = amountValue > attrs.amountMinValue
            increaseBtn.isEnabled = true
            AmountValueChangedCallback?.let { it(amountValue) }
        }


        increaseBtn.setOnClickListener {
            amountValue++
            amountView.text = amountValue.toString()
            increaseBtn.isEnabled = amountValue < attrs.amountMaxValue
            decreaseBtn.isEnabled = true
            AmountValueChangedCallback?.let { it(amountValue) }
        }
    }

    private fun generateAmountTextView(): TextView {
        val textView = TextView(context).apply {
            setPadding(0)
            textSize = attrs.amountTextSize
            setTextColor(attrs.amountTextColor)
            setBackgroundColor(attrs.amountBackground)
            includeFontPadding = false
            gravity = Gravity.CENTER
            minWidth = attrs.amountSize
        }
        val layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT)
        layoutParams.leftMargin = attrs.margin
        layoutParams.rightMargin = attrs.margin

        textView.layoutParams = layoutParams
        return textView
    }

    private fun generateButton(): Button {
        val button = Button(context).apply {
            includeFontPadding = false
            setPadding(0)
            setBackgroundResource(0)
            setTextColor(attrs.btnTextColor)
            setTextColor(attrs.btnTextColor)
            setTextSize(TypedValue.COMPLEX_UNIT_PX, attrs.btnTextSize)
            gravity = Gravity.CENTER
        }
        button.layoutParams = LayoutParams(attrs.btnSize, attrs.btnSize)
        return button
    }

    fun setAmountValueChangedListener(callback: (Int) -> Unit) {
        this.AmountValueChangedCallback = callback
    }

    fun gtAmountValue(): Int {
        return amountValue
    }
}