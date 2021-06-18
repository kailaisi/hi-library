package com.kailaisi.hi_ui.date_item

import android.content.Context
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import java.lang.RuntimeException
import java.lang.ref.WeakReference
import java.lang.reflect.ParameterizedType

/**
 * 描述：
 * <p/>作者：kailaisi
 * <br/>创建时间：2021-05-27:21:50
 */
class HiAdapter(context: Context) : RecyclerView.Adapter<ViewHolder>() {

    private var recyclerViewRef: WeakReference<RecyclerView>? = null
    private var mContext: Context? = null
    private var mInflater: LayoutInflater? = null
    private var dataSets = ArrayList<HiDataItem<*, out ViewHolder>>()

    private var typeArray = SparseArray<HiDataItem<*, out ViewHolder>>()

    /**
     * 保存header数据，其中key是自增的，并且作为viewType的值，value则是具体的布局
     */
    private var headers = SparseArray<View>()
    private var BASE_ITEM_TYPE_HEADER = 100000

    private var foots = SparseArray<View>()
    private var BASE_ITEM_TYPE_FOOT = 200000

    init {
        this.mContext = context
        this.mInflater = LayoutInflater.from(context)
    }

    fun addHeader(view: View) {
        if (headers.indexOfValue(view) < 0) {
            headers.put(BASE_ITEM_TYPE_HEADER++, view)
            notifyItemInserted(headers.size() - 1)
        }
    }

    fun removeHeader(view: View) {
        val indexOfValue = headers.indexOfValue(view)
        if (indexOfValue < 0) return
        headers.removeAt(indexOfValue)
        notifyItemRemoved(indexOfValue)
    }

    fun addFooter(view: View) {
        if (foots.indexOfValue(view) < 0) {
            foots.put(BASE_ITEM_TYPE_FOOT++, view)
            notifyItemInserted(itemCount)
        }
    }

    fun removeFooter(view: View) {
        val indexOfValue = foots.indexOfValue(view)
        if (indexOfValue < 0) return
        foots.removeAt(indexOfValue)
        //找到在列表中的位置
        notifyItemRemoved(indexOfValue + getHeaderSize() + getOriginalItemSize())
    }

    private fun getHeaderSize(): Int {
        return headers.size()
    }

    private fun getFooterSize(): Int {
        return foots.size()
    }

    private fun getOriginalItemSize(): Int {
        return dataSets.size
    }

    /**
     *在指定为上添加HiDataItem
     */
    fun addItemAt(
        index: Int,
        dataItem: HiDataItem<*, out ViewHolder>,
        notify: Boolean,
    ) {
        if (index >= 0) {
            dataSets.add(index, dataItem)
        } else {
            dataSets.add(dataItem)
        }

        val notifyPos = if (index >= 0) index else dataSets.size - 1
        if (notify) {
            notifyItemInserted(notifyPos)
        }

        dataItem.setAdapter(this)
    }

    fun addItems(items: List<HiDataItem<*, out ViewHolder>>, notify: Boolean) {
        val start = dataSets.size
        for (item in items) {
            dataSets.add(item)
        }
        if (notify) {
            notifyItemRangeChanged(start, items.size)
        }
    }

    fun remove(index: Int): HiDataItem<*, out ViewHolder>? {
        if (index > 0 && index < dataSets.size) {
            val removeAt = dataSets.removeAt(index)
            notifyItemRemoved(index)
            return removeAt
        }
        return null
    }

    fun remove(data: HiDataItem<*, *>?) {
        if (data != null) {
            val indexOf = dataSets.indexOf(data)
            remove(indexOf)
        }
    }

    override fun getItemCount(): Int {
        return dataSets.size + getFooterSize() + getHeaderSize()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (headers.indexOfKey(viewType) >= 0) {
            val view = headers[viewType]
            return object : ViewHolder(view) {}
        }
        if (foots.indexOfKey(viewType) >= 0) {
            val view = foots[viewType]
            return object : ViewHolder(view) {}
        }
        val dataItem = typeArray.get(viewType)
        var itemView = dataItem.getItemView(parent)
        if (itemView == null) {
            val layout = dataItem.getItemLayoutRes()
            if (layout < 0) {
                throw  RuntimeException("dataItem:${dataItem.javaClass.name} must override getItemView or getItemLayoutRes")
            }
            itemView = mInflater!!.inflate(layout, parent, false)
        }
        return createViewHolderInternal(dataItem.javaClass, itemView)
    }

    private fun createViewHolderInternal(
        javaClass: Class<HiDataItem<*, out ViewHolder>>,
        itemView: View?,
    ): ViewHolder {
        val superclass = javaClass.genericSuperclass
        if (superclass is ParameterizedType) {
            val arguments = superclass.actualTypeArguments
            arguments.forEach {
                if (it is Class<*> && ViewHolder::class.java.isAssignableFrom(it)) {
                    return it.getConstructor(View::class.java)
                        .newInstance(itemView) as ViewHolder
                }
            }
        }
        return object : ViewHolder(itemView!!) {}
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (isHeaderPosition(position) || isFooterPosition(position)) {
            return
        }
        val itemPos = position - getHeaderSize()
        val item = getItem(itemPos)
        item?.onBindData(holder, itemPos)
    }


    override fun getItemViewType(position: Int): Int {
        if (isHeaderPosition(position)) {
            return headers.keyAt(position)
        }
        if (isFooterPosition(position)) {
            val footerPos = position - getHeaderSize() - getOriginalItemSize()
            return foots.keyAt(footerPos)
        }
        val itemPos = position - getHeaderSize()
        val type = dataSets[itemPos].javaClass.hashCode()
        if (typeArray.indexOfKey(type) < 0) {
            typeArray.put(type, dataSets[itemPos])
        }
        return type
    }

    private fun isFooterPosition(position: Int): Boolean {
        return position >= headers.size() + getOriginalItemSize()
    }

    private fun isHeaderPosition(position: Int): Boolean {
        return position < headers.size()
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        recyclerViewRef = WeakReference(recyclerView)
        //为列表上的item适配网格布局，每个item可以设置占据多个网格。
        val layoutManager = recyclerView.layoutManager
        if (layoutManager is GridLayoutManager) {
            val spanCount = layoutManager.spanCount
            layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    if (isHeaderPosition(position) || isFooterPosition(position)) {
                        return spanCount
                    }
                    val itemPos = position - getHeaderSize()
                    if (itemPos < dataSets.size) {
                        val spanSize = dataSets[itemPos].getSpanSize()
                        return if (spanSize <= 0) spanCount else spanSize
                    }
                    return spanCount
                }

            }
        }
    }

    override fun onViewAttachedToWindow(holder: ViewHolder) {
        getAttachRecyclerView()?.let {
            //瀑布流的item占比适配
            val position = it.getChildAdapterPosition(holder.itemView)
            val b = isHeaderPosition(position) || isFooterPosition(position)
            val itemPos = position - getHeaderSize()
            val dataItem = getItem(itemPos) ?: return
            val lp = holder.itemView.layoutParams
            if (lp != null && lp is StaggeredGridLayoutManager.LayoutParams) {
                val spanSize = dataItem.getSpanSize()
                val staggeredGridLayoutManager = it.layoutManager as StaggeredGridLayoutManager
                if (b) {
                    lp.isFullSpan = true
                }
                if (spanSize == staggeredGridLayoutManager.spanCount) {
                    lp.isFullSpan = true
                }
            }
            dataItem.onViewAttachedToWindow(holder)
        }
    }

    private fun getItem(position: Int): HiDataItem<*, ViewHolder>? {
        if (position < 0 || position >= dataSets.size) {
            return null
        }
        return dataSets[position] as HiDataItem<*, ViewHolder>
    }

    fun getAttachRecyclerView(): RecyclerView? {
        return recyclerViewRef?.get()
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        val position = holder.adapterPosition
        if (isHeaderPosition(position) || isFooterPosition(position)) {
            return
        }
        val itemPosition = position - getHeaderSize()
        val data = getItem(itemPosition) ?: return
        data.onViewDetachedFromWindow(holder)
    }

    fun refreshItem(hiDataItem: HiDataItem<*, *>) {
        val indexOf = dataSets.indexOf(hiDataItem)
        notifyItemChanged(indexOf)
    }

    fun clearItems() {
        dataSets.clear()
        notifyDataSetChanged()
    }

}