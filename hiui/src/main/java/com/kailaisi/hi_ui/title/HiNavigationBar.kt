package com.kailaisi.hi_ui.title

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.annotation.StringRes
import com.kailaisi.hi_ui.R
import com.kailaisi.hi_ui.icfont.IconFontButton
import com.kailaisi.hi_ui.icfont.IconFontTextView
import com.kailaisi.library.util.HiDisplayUtils
import com.kailaisi.library.util.HiRes

/**
 * 描述：自定义标题栏。支持右侧动态添加多个按钮
 * 1.中间文字居中
 * 2.右侧按钮，要动态的设置间距padding
 * 3.动态计算左右侧按钮宽度，通过动态测量，使中间的文字居中
 * <p/>作者：wu
 * <br/>创建时间：2021-07-03:15:19
 */
class HiNavigationBar @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attributeSet, defStyleAttr) {
    private var navAttr: Attrs

    /**
     * 做左侧的view的id
     */
    private var mLeftViewId = View.NO_ID

    /**
     * 最右侧的view的id
     */
    private var mRightViewId = View.NO_ID
    private val mLeftViewList = ArrayList<View>()
    private val mRightViewList = ArrayList<View>()

    private var titleView: IconFontTextView? = null
    private var subTitleView: IconFontTextView? = null

    //title的包裹容器
    private var titleContainer: LinearLayout? = null

    init {
        navAttr = parseNavAttrs(context, attributeSet, defStyleAttr)
        if (!navAttr.navTitle.isNullOrEmpty()) {
            setTitle(navAttr.navTitle!!)
        }
        if (!navAttr.navSubtitle.isNullOrEmpty()) {
            setSubtitle(navAttr.navSubtitle!!)
        }
    }

    fun setNavListener(listener: OnClickListener) {
        if (!navAttr.navIconStr.isNullOrEmpty()) {
            val back = addLeftTextButton(navAttr.navIconStr!!, R.integer.id_left_back_view)
            back.setOnClickListener(listener)
        }
    }

    /**
     * 添加一个显示文字的View
     */
    fun addLeftTextButton(@StringRes string: Int, viewId: Int): Button {
        return addLeftTextButton(HiRes.getString(string), viewId)
    }

    /**
     * 添加一个显示文字的View
     */
    fun addLeftTextButton(text: String, viewId: Int): Button {
        val button = generateTextButton()
        button.text = text
        button.id = viewId
        if (mLeftViewList.isEmpty()) {
            //为了使button的按钮间距相同，那么最左边的按钮要是2个padding的间距
            button.setPadding(navAttr.horPadding * 2, 0, navAttr.horPadding, 0)
        } else {
            button.setPadding(navAttr.horPadding, 0, navAttr.horPadding, 0)
        }
        addLeftView(button, generateTextButtonLayoutParams())
        return button
    }

    /**
     * 动态添加一个View
     */
    fun addLeftView(view: View, layoutParams: LayoutParams) {
        val viewId = view.id
        if (viewId == View.NO_ID) {
            throw IllegalArgumentException("left view must has an unique id.")
        }
        //设置在布局中的相对位置
        if (mLeftViewId == View.NO_ID) {
            layoutParams.addRule(ALIGN_PARENT_LEFT, viewId)
        } else {
            layoutParams.addRule(RIGHT_OF, mLeftViewId)
        }
        mLeftViewId = view.id
        layoutParams.alignWithParent = true
        mLeftViewList.add(view)
        addView(view,layoutParams)
    }


    /**
     * 添加一个显示文字的View
     */
    fun addRightTextButton(@StringRes string: Int, viewId: Int): Button {
        return addRightTextButton(HiRes.getString(string), viewId)
    }

    /**
     * 添加一个显示文字的View
     */
    fun addRightTextButton(text: String, viewId: Int): Button {
        val button = generateTextButton()
        button.text = text
        button.id = viewId
        if (mRightViewList.isEmpty()) {
            //为了使button的按钮间距相同，那么最左边的按钮要是2个padding的间距
            button.setPadding(navAttr.horPadding, 0, navAttr.horPadding * 2, 0)
        } else {
            button.setPadding(navAttr.horPadding, 0, navAttr.horPadding, 0)
        }
        addRightView(button, generateTextButtonLayoutParams())
        return button
    }

    /**
     * 动态添加一个View
     */
    fun addRightView(view: View, layoutParams: LayoutParams) {
        val viewId = view.id
        if (viewId == View.NO_ID) {
            throw IllegalArgumentException("Right view must has an unique id.")
        }
        //设置在布局中的相对位置
        if (mRightViewId == View.NO_ID) {
            layoutParams.addRule(ALIGN_PARENT_RIGHT, viewId)
        } else {
            layoutParams.addRule(LEFT_OF, mRightViewId)
        }
        mRightViewId = view.id
        layoutParams.alignWithParent = true
        mRightViewList.add(view)
        addView(view,layoutParams)
    }

    fun setTitle(title: String) {
        ensureTitle()
        titleView?.text = title
        titleView?.visibility = if (title.isNullOrBlank()) View.GONE else View.VISIBLE
    }

    fun setSubtitle(title: String) {
        ensureSubtitle()
        updateTitleViewStyle()
        subTitleView?.text = title
        subTitleView?.visibility = if (title.isNullOrBlank()) View.GONE else View.VISIBLE
    }

    private fun ensureTitle() {
        if (titleView == null) {
            titleView = IconFontTextView(context, null).apply {
                gravity = Gravity.CENTER
                isSingleLine = true
                ellipsize = TextUtils.TruncateAt.END
                setTextColor(navAttr.titleTextColor)
            }
            ensureContainerStyle()
            updateTitleViewStyle()
            titleContainer?.addView(titleView, 0)
        }
    }

    private fun ensureSubtitle() {
        if (subTitleView == null) {
            subTitleView = IconFontTextView(context, null).apply {
                gravity = Gravity.CENTER
                isSingleLine = true
                ellipsize = TextUtils.TruncateAt.END
                setTextColor(navAttr.subTitleColor)
                textSize = navAttr.subTitleSize
            }
            ensureContainerStyle()
            titleContainer?.addView(subTitleView)
        }
    }

    /**
     * 初始化标题容器样式
     */
    private fun ensureContainerStyle() {
        if (titleContainer == null) {
            titleContainer = LinearLayout(context, null).apply {
                gravity = Gravity.CENTER
                orientation = LinearLayout.VERTICAL
            }
            val params =
                LayoutParams(LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT)
            params.addRule(CENTER_IN_PARENT)
            this@HiNavigationBar.addView(titleContainer, params)
        }
    }

    /**
     * 更新主标题样式
     */
    private fun updateTitleViewStyle() {
        if (titleView != null) {
            if (subTitleView == null || TextUtils.isEmpty(subTitleView!!.text)) {
                titleView?.setTextSize(TypedValue.COMPLEX_UNIT_PX, navAttr.titleTextSize)
                titleView?.typeface = Typeface.DEFAULT_BOLD
            } else {
                titleView?.setTextSize(
                    TypedValue.COMPLEX_UNIT_PX,
                    navAttr.titleTextSizeWithSubtitle
                )
                titleView?.typeface = Typeface.DEFAULT
            }
        }
    }

    private fun generateTextButtonLayoutParams(): LayoutParams {
        return LayoutParams(LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (titleContainer != null) {
            var paddingLeft = paddingLeft
            for (view in mLeftViewList) {
                paddingLeft += view.measuredWidth
            }
            var paddingRight = paddingRight
            for (view in mRightViewList) {
                paddingRight += view.measuredWidth
            }
            //
            val titleContainerWidth = titleContainer!!.measuredWidth
            //让标题居中
            val remain = measuredWidth - Math.max(paddingLeft, paddingRight) * 2
            if (remain < titleContainerWidth) {
                //直接设置具体的宽度
                val widthSize = MeasureSpec.makeMeasureSpec(remain, MeasureSpec.EXACTLY)
                titleContainer!!.measure(widthSize, heightMeasureSpec)
            }
        }
    }

    private fun generateTextButton(): IconFontButton {
        val button = IconFontButton(context)
        button.setBackgroundResource(0)
        button.apply {
            minWidth = 0
            minimumWidth = 0
            minHeight = 0
            minimumHeight = 0

            gravity = Gravity.CENTER
        }
        button.setTextSize(TypedValue.COMPLEX_UNIT_PX, navAttr.btnTextSize)
        button.setTextColor(navAttr.btnTextColor)
        return button
    }

    private fun parseNavAttrs(
        context: Context,
        attributeSet: AttributeSet?,
        defStyleAttr: Int
    ): Attrs {
        //如果我们这里使用了 R.style.navigationStyle，那么如果样式不符合要求，则用户每次使用都需要自定义所有的样式。
        // 我们这里通过一个自定义的属性值，允许用户在主题中设置对应的整体style样式
        val value = TypedValue()
        //从主题中获取设置的navigationViewStyle样式。
        context.theme.resolveAttribute(R.attr.hiNavigationStyle, value, true)
        //如果用户自己设置了，则使用用户设置的，如果没有，则使用默认的
        val defStyle = if (value.resourceId != 0) value.resourceId else R.style.navigationStyle
        val attr = context.obtainStyledAttributes(
            attributeSet,
            R.styleable.HiNavigationBar,
            defStyleAttr,
            defStyle
        )
        //优先级：xml->theme->default设置
        val navIcon = attr.getString(R.styleable.HiNavigationBar_nav_icon)
        val title = attr.getString(R.styleable.HiNavigationBar_nav_title)
        val subTitle = attr.getString(R.styleable.HiNavigationBar_nav_subtitle)
        val horPadding = attr.getDimensionPixelSize(R.styleable.HiNavigationBar_hor_padding, 0)
        val btnTextSize = attr.getDimensionPixelSize(
            R.styleable.HiNavigationBar_text_btn_text_size,
            HiDisplayUtils.sp2px(16f)
        )
        //这里使用colorstate，是为了能够兼容处理按钮有点击和未点击的时候的颜色区别
        val btnTextColor = attr.getColorStateList(R.styleable.HiNavigationBar_text_btn_text_color)
        val titleTextSize = attr.getDimensionPixelSize(
            R.styleable.HiNavigationBar_title_text_size,
            HiDisplayUtils.sp2px(17f)
        )
        //当有副标题的时候，主标题的字体大小
        val titleTextSizeWithSubtitle = attr.getDimensionPixelSize(
            R.styleable.HiNavigationBar_title_text_size_with_subTitle,
            HiDisplayUtils.sp2px(15f)
        )

        val titleTextColor = attr.getColor(
            R.styleable.HiNavigationBar_title_text_color,
            HiRes.getColor(R.color.hi_tabtop_normal_color)
        )
        val subTitleSize = attr.getDimensionPixelSize(
            R.styleable.HiNavigationBar_subTitle_text_size,
            HiDisplayUtils.sp2px(14f)
        )
        val subTitleColor = attr.getColor(
            R.styleable.HiNavigationBar_subTitle_text_color,
            HiRes.getColor(R.color.hi_tabtop_normal_color)
        )
        attr.recycle()
        return Attrs(
            navIconStr = navIcon,
            navTitle = title,
            navSubtitle = subTitle,
            horPadding = horPadding,
            btnTextColor = btnTextColor,
            btnTextSize = btnTextSize.toFloat(),
            titleTextSize = titleTextSize.toFloat(),
            titleTextSizeWithSubtitle = titleTextSizeWithSubtitle.toFloat(),
            titleTextColor = titleTextColor,
            subTitleColor = subTitleColor,
            subTitleSize = subTitleSize.toFloat()
        )
    }


    private data class Attrs(
        val navIconStr: String?,
        val navTitle: String?,
        val navSubtitle: String?,
        val horPadding: Int,
        val btnTextSize: Float,
        val btnTextColor: ColorStateList?,
        val titleTextSize: Float,
        val titleTextSizeWithSubtitle: Float,
        val titleTextColor: Int,
        val subTitleSize: Float,
        val subTitleColor: Int,
    )
}