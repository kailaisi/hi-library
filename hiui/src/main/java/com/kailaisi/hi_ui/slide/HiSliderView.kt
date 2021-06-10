package com.kailaisi.hi_ui.slide

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.kailaisi.hi_ui.R
import com.kailaisi.hi_ui.date_item.HiViewHolder
import com.kailaisi.library.util.HiDisplayUtils

/**
 * 左右数据
 */
class HiSliderView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0,
) : LinearLayout(context, attributeSet, defStyle) {
    private var menuItemAttr: MenuItemAttr
    private val MENU_TEIM_LAYOUT_RES_ID = R.layout.hi_slider_menu_item
    private val CONTENT_TEIM_LAYOUT_RES_ID = R.layout.hi_slider_content_item

    /*左侧数据*/
    val menuView = RecyclerView(context)

    /*右侧数据*/
    val contentView = RecyclerView(context)

    init {
        menuItemAttr = parseAttrs(attributeSet)
        orientation = HORIZONTAL
        menuView.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT)
        menuView.overScrollMode = View.OVER_SCROLL_NEVER
        menuView.itemAnimator = null
        contentView.layoutParams =
            LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        contentView.overScrollMode = View.OVER_SCROLL_NEVER
        contentView.itemAnimator = null

        addView(menuView)
        addView(contentView)
    }


    fun bindMenuView(
        layoutRes: Int = MENU_TEIM_LAYOUT_RES_ID,
        itemCount: Int,
        onBindView: (HiViewHolder, Int) -> Unit,
        onItemClick: ((HiViewHolder, Int) -> Unit)? = null,
    ) {
        menuView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        menuView.adapter = MenuAdapter(layoutRes, itemCount, onBindView, onItemClick)
    }


    fun bindContentView(
        layoutRes: Int = CONTENT_TEIM_LAYOUT_RES_ID,
        itemCount: Int,
        layoutManager: RecyclerView.LayoutManager,
        itemDecoration: RecyclerView.ItemDecoration? = null,
        onBindView: (HiViewHolder, Int) -> Unit,
        onItemClick: ((HiViewHolder, Int) -> Unit)? = null,
    ) {
        if (contentView.layoutManager == null) {
            contentView.layoutManager = layoutManager
            contentView.adapter = ContentAdapter(layoutRes)
            itemDecoration?.let { contentView.addItemDecoration(it) }
        }
        val contentAdapter = contentView.adapter as ContentAdapter
        contentAdapter.update(itemCount, onBindView, onItemClick)
        /*自动滚动到第一个*/
        contentView.scrollToPosition(0)
    }

    inner class ContentAdapter(private val layoutRes: Int) : RecyclerView.Adapter<HiViewHolder>() {
        private var count: Int = 0
        lateinit var onBindView: (HiViewHolder, Int) -> Unit
        var onItemClick: ((HiViewHolder, Int) -> Unit)? = null

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HiViewHolder {
            val view = LayoutInflater.from(context).inflate(layoutRes, parent, false)
            val remainSpace = width - paddingLeft - paddingBottom - menuItemAttr.width
            val layoutManager = (parent as RecyclerView).layoutManager
            var spanCount = 0
            if (layoutManager is GridLayoutManager) {
                spanCount = layoutManager.spanCount
            } else if (layoutManager is StaggeredGridLayoutManager) {
                spanCount = layoutManager.spanCount
            }
            if (spanCount > 0) {
                val itemWidth = remainSpace / spanCount
                /*为了防止加载完图片之后再展示，导致的页面上下闪动问题，我们这里直接设置好宽高*/
                view.layoutParams = RecyclerView.LayoutParams(itemWidth, itemWidth)
            }
            val holder = HiViewHolder(view)
            holder.itemView.setOnClickListener {
                onItemClick?.invoke(holder, holder.layoutPosition)
            }
            return holder
        }

        override fun onBindViewHolder(holder: HiViewHolder, position: Int) {
            onBindView.invoke(holder, position)
        }

        override fun getItemCount(): Int {
            return count
        }

        fun update(
            itemCount: Int,
            onBindView: (HiViewHolder, Int) -> Unit,
            onItemClick: ((HiViewHolder, Int) -> Unit)?,
        ) {
            this.count = itemCount
            this.onBindView = onBindView
            this.onItemClick = onItemClick
            notifyDataSetChanged()
        }

    }

    private fun parseAttrs(attr: AttributeSet?): MenuItemAttr {
        val typedArray = context.obtainStyledAttributes(attr, R.styleable.HiSliderView)
        val width =
            typedArray.getDimensionPixelOffset(R.styleable.HiSliderView_menuItemWidth,
                HiDisplayUtils.dip2px(context, 100f))
        val height =
            typedArray.getDimensionPixelOffset(R.styleable.HiSliderView_menuItemHeight,
                HiDisplayUtils.dip2px(context, 45f))
        val textSize = typedArray.getDimensionPixelSize(R.styleable.HiSliderView_menuItemTextSize,
            HiDisplayUtils.sp2px(context, 14f))
        val selectTextSize =
            typedArray.getDimensionPixelSize(R.styleable.HiSliderView_menuItemTextSize,
                HiDisplayUtils.sp2px(context, 14f))

        val textColor = typedArray.getColorStateList(R.styleable.HiSliderView_menuItemTextColor)
            ?: generateColorState()

        val indicator = typedArray.getDrawable(R.styleable.HiSliderView_menuItemIndicator)
            ?: ContextCompat.getDrawable(context, R.drawable.shape_hi_slider_indicator)

        val backgroundColor = typedArray.getColor(R.styleable.HiSliderView_menuItemBackgroundColor,
            Color.parseColor("#ffffff"))
        val selectedBackgroundColor =
            typedArray.getColor(R.styleable.HiSliderView_menuItemSelectedBackgroundColor,
                Color.parseColor("#ffffff"))
        typedArray.recycle()
        return MenuItemAttr(width,
            height,
            textColor,
            textSize,
            selectTextSize,
            backgroundColor,
            selectedBackgroundColor,
            indicator)
    }


    private fun generateColorState(): ColorStateList {
        val status = Array(2) { intArrayOf(2) }
        val colors = intArrayOf(Color.parseColor("#ffff00"), Color.parseColor("#333333"))
        status[0] = intArrayOf(android.R.attr.state_selected)
        status[1] = intArrayOf(1)
        return ColorStateList(status, colors)
    }


    inner class MenuAdapter(
        val layoutRes: Int,
        val itemCounts: Int,
        val onBindView: (HiViewHolder, Int) -> Unit,
        val onItemClick: ((HiViewHolder, Int) -> Unit)?,
    ) : RecyclerView.Adapter<HiViewHolder>() {
        private var lastSelectIndex: Int = 0
        private var currentSelectIndex: Int = 0

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HiViewHolder {
            val rootView = LayoutInflater.from(parent.context).inflate(layoutRes, parent)
            val params = RecyclerView.LayoutParams(menuItemAttr.width, menuItemAttr.height)
            rootView.layoutParams = params
            rootView.setBackgroundColor(menuItemAttr.backgroundColor)
            rootView.findViewById<TextView>(R.id.menu_item_text)
                ?.setTextColor(menuItemAttr.textColor)
            rootView.findViewById<ImageView>(R.id.menu_item_indicator)
                ?.setImageDrawable(menuItemAttr.indicator)
            val holder = HiViewHolder(rootView)
            holder.itemView.setOnClickListener {
                currentSelectIndex = holder.layoutPosition
                notifyItemChanged(currentSelectIndex)
                notifyItemChanged(lastSelectIndex)
            }
            return holder
        }

        private fun applyItemAttr(position: Int, holder: ViewHolder) {
            val selected = position == currentSelectIndex
            val titleView: TextView? = holder.itemView.findViewById(R.id.menu_item_text)
            val indicator: ImageView? = holder.itemView.findViewById(R.id.menu_item_indicator)
            titleView?.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                if (selected) menuItemAttr.selectTextSize.toFloat() else menuItemAttr.textSize.toFloat())
            holder.itemView.setBackgroundColor(if (selected) menuItemAttr.selectBackgroundColor else menuItemAttr.backgroundColor)
            titleView?.isSelected = selected
            indicator?.visibility = if (selected) View.VISIBLE else View.GONE
        }

        override fun onBindViewHolder(holder: HiViewHolder, position: Int) {
            applyItemAttr(position, holder)
            if (currentSelectIndex == position) {
                /*放到这里，是为了第一次的时候，就能够出发事件，然后将第一个右侧的数据加载出来*/
                onItemClick?.invoke(holder, position)
                lastSelectIndex = currentSelectIndex
            }
            onBindView.invoke(holder, position)
        }

        override fun getItemCount(): Int {
            return itemCounts
        }

    }
}
