package com.kailaisi.hi_ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.InputType
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView

/**
 * 自定义View
 */
class InputItemLayout : LinearLayout {
    private var bottomLine: Line

    var topLine: Line
        private set
    var editText: EditText
        private set
    var textView: TextView
        private set

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyle: Int) : super(context,
        attributeSet,
        defStyle) {
        orientation = HORIZONTAL
        val array = context.obtainStyledAttributes(attributeSet, R.styleable.InputItemLayout)
        /*文字*/
        val titleStyleId = array.getResourceId(R.styleable.InputItemLayout_titleTextAppearance, 0)
        val title = array.getString(R.styleable.InputItemLayout_title)
        textView = parseTitleStyle(title, titleStyleId)

        /*输入框*/
        val inputStyleId = array.getResourceId(R.styleable.InputItemLayout_inputTextAppearance, 0)
        val inputType = array.getInt(R.styleable.InputItemLayout_inputType, 0)
        val hint = array.getString(R.styleable.InputItemLayout_hint)
        editText = parseInputStyle(hint, inputType, inputStyleId)
        /*分割线*/
        val topLineStyleId = array.getResourceId(R.styleable.InputItemLayout_topLineAppearance, 0)
        val bottomLineStyleId =
            array.getResourceId(R.styleable.InputItemLayout_bottomLineAppearance, 0)
        topLine = parseLineStyle(topLineStyleId)
        bottomLine = parseLineStyle(bottomLineStyleId)

        if (topLine.enable) {
            topPaint.style = Paint.Style.FILL_AND_STROKE
            topPaint.color = topLine.color
            topPaint.strokeWidth = topLine.height.toFloat()
        }
        if (bottomLine.enable) {
            bottomPaint.style = Paint.Style.FILL_AND_STROKE
            bottomPaint.color = bottomLine.color
            bottomPaint.strokeWidth = bottomLine.height.toFloat()
        }

        array.recycle()
    }


    /**
     * 分割线属性解析
     */
    private fun parseLineStyle(topLineStyleId: Int): Line {
        val array = context.obtainStyledAttributes(topLineStyleId, R.styleable.lineAppearance)
        val line = Line()
        line.color =
            array.getColor(R.styleable.lineAppearance_color, resources.getColor(R.color.color_d1d2))
        line.enable = array.getBoolean(R.styleable.lineAppearance_enable, false)
        line.height = array.getDimensionPixelSize(R.styleable.lineAppearance_height, 0)
        line.marginLeft = array.getDimensionPixelSize(R.styleable.lineAppearance_leftMargin, 0)
        line.marginRight = array.getDimensionPixelSize(R.styleable.lineAppearance_rightMargin, 0)
        array.recycle()
        return line
    }


    inner class Line {
        var color = 0
        var height = 0
        var marginLeft = 0
        var marginRight = 0
        var enable = false
    }


    var topPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    var bottomPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (topLine.enable) {
            canvas?.drawLine(topLine.marginLeft.toFloat(), 0f,
                (measuredWidth - topLine.marginRight).toFloat(), topLine.height.toFloat(), topPaint)
        }
        if (bottomLine.enable) {
            canvas?.drawLine(bottomLine.marginLeft.toFloat(),
                (height - bottomLine.height).toFloat(),
                (measuredWidth - bottomLine.marginRight).toFloat(),
                (height - bottomLine.height).toFloat(), bottomPaint)

        }
    }

    /**
     * 解析输入框属性
     */
    private fun parseInputStyle(hint: String?, inputType: Int, inputStyleId: Int): EditText {
        val array = context.obtainStyledAttributes(inputStyleId, R.styleable.inputTextAppearance)
        val hintColor = array.getColor(R.styleable.inputTextAppearance_hintColor,
            resources.getColor(R.color.color_d1d2))
        val textColor = array.getColor(R.styleable.inputTextAppearance_inputColor,
            resources.getColor(R.color.color_565))
        val textSize = array.getDimensionPixelSize(R.styleable.inputTextAppearance_textSize,
            applyUnit(TypedValue.COMPLEX_UNIT_SP, 14f))
        val editText = EditText(context)
        val param = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT)
        param.weight = 1f
        editText.layoutParams = param
        editText.setHint(hint)
        editText.setHintTextColor(hintColor)
        editText.setTextColor(textColor)
        editText.setBackgroundColor(Color.TRANSPARENT)
        editText.gravity = Gravity.START and (Gravity.CENTER)
        if (inputType == 0) {
            editText.inputType = InputType.TYPE_TEXT_VARIATION_NORMAL
        } else if (inputType == 1) {
            editText.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT
        } else {
            editText.inputType = InputType.TYPE_NUMBER_VARIATION_NORMAL
        }
        editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize.toFloat())
        addView(editText)
        array.recycle()
        return editText
    }


    private fun applyUnit(unit: Int, value: Float): Int {
        return TypedValue.applyDimension(unit, value, resources.displayMetrics).toInt()
    }

    /**
     * 解析Title资源属性
     */
    private fun parseTitleStyle(title: String?, titleStyleId: Int): TextView {
        val array = context.obtainStyledAttributes(titleStyleId, R.styleable.titleTextAppearance)
        val textColor = array.getColor(R.styleable.titleTextAppearance_titleColor,
            resources.getColor(R.color.color_565))
        val textSize = array.getDimensionPixelSize(R.styleable.titleTextAppearance_titleSize,
            applyUnit(TypedValue.COMPLEX_UNIT_SP, 15f))
        val minWidth = array.getDimensionPixelSize(R.styleable.titleTextAppearance_minWidth, 0)
        val textView = TextView(context)
        val param = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        textView.layoutParams = param
        textView.gravity = Gravity.START and (Gravity.CENTER)
        textView.setTextColor(textColor)
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize.toFloat())
        textView.setBackgroundColor(Color.TRANSPARENT)
        textView.minWidth = minWidth
        textView.text = title
        addView(textView)
        array.recycle()
        return textView
    }


}