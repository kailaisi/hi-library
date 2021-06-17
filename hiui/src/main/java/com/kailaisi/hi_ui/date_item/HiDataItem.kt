package com.kailaisi.hi_ui.date_item

import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

/**
 * 描述：数据类的抽象类，
 *
 * 作者：kailaisi
 * <br></br>创建时间：2021-05-27:21:45
 */
abstract class HiDataItem<DATA, VH : RecyclerView.ViewHolder>(data: DATA?=null) {
    private lateinit var adapter: HiAdapter
    var mData: DATA? = null

    init {
        this.mData = data
    }


    /**
     * 进行数据绑定
     */
    abstract fun onBindData(holder:VH, position: Int)

    /**
     * 返回Item的对应的布局资源
     */
    open fun getItemLayoutRes(): Int {
        return -1
    }

    /**
     * 返回该Item的视图View
     */
    open   fun getItemView(parent: ViewGroup): View? {
        return null
    }

    fun setAdapter(adapter: HiAdapter) {
        this.adapter = adapter
    }

    /**
     * 刷新列表
     */
    fun refreshItem() {
        adapter.refreshItem(this)
    }

    /**
     * 移除
     */
    fun removeItem() {
        adapter.remove(this)
    }

    /**
     * 该Item占据几列
     */
    open  fun getSpanSize(): Int {
        return 0
    }

    fun onViewAttachedToWindow(holder: VH) {
    }

    fun onViewDetachedFromWindow(holder: VH) {
    }
}