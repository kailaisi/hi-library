package com.kailaisi.hi_ui.recyclerview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.kailaisi.hi_ui.R
import com.kailaisi.hi_ui.date_item.HiAdapter
import com.kailaisi.library.log.HiLog

/**
 * 描述：支持上拉加载更多的RecyclerView
 * <p/>作者：kailaisi
 * <br/>创建时间：2021-05-29:22:38
 */
class HiRecyclerView @JvmOverloads constructor(
    context: Context,
    attributes: AttributeSet? = null,
    defstyle: Int = 0,
) : RecyclerView(context, attributes, defstyle) {
    private var isLoadingMore: Boolean = false
    private var loadMoreListener: LoadMoreListener? = null
    private var footerView: View?=null

    inner class LoadMoreListener(val prefetchSize: Int, val callback: () -> Unit) :
        OnScrollListener() {

        val adapter = getAdapter() as HiAdapter
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            //需要根据当前的滑动状态，决定需不需要添加footer view，要不执行上拉加载分页的操作
            if (isLoadingMore) {
                return
            }
            //获取当前类表上已经显示的item的个数，
            val itemCount = adapter.itemCount
            if (itemCount <= 0) {
                return
            }
            //应该在滑动状态为拖动状态时，判断要不要添加footer
            //1.判断列表是否能够滑动，可以通过是否可以滑动来判断
            //2。判断当前是否是最后一个
            val canScrollVertically = recyclerView.canScrollVertically(1)
            var lastVisible = findLastVisiblePosition(recyclerView)
            if (lastVisible <= 0) {
                return
            }
            var isBottom = lastVisible > itemCount - 1
            //可以向下滑动，或者已经到最底部了，此时再拖动列表，也是允许分页的
            if (newState === SCROLL_STATE_DRAGGING && (canScrollVertically || isBottom)) {
                addFooterView()
            }
            //不能在滑动停止之后，添加
            if (newState != SCROLL_STATE_IDLE) {
                return
            }
            //预加载,如果到达了预加载的阈值，则进行回调
            val arrivePreFetchPosition = itemCount - lastVisible <= prefetchSize
            if (arrivePreFetchPosition) {
                return
            }
            isLoadingMore = true
            callback.invoke()
        }

        /**
         * 添加加载更多的视图，这里需要防止重复添加
         */
        private fun addFooterView() {
            //为了防止重复添加，每次都remove。但是remove从recyclerview移除之后，需要等下一个绘制才可以,直接执行add，会导致数据错误
            if (footerView==null) {
                footerView=LayoutInflater.from(context).inflate(R.layout.layout_footer_loading, this@HiRecyclerView, false)
            }
            val parent = footerView!!.parent
            if (parent != null) {
                //不断调用自己，直到parent为空，也就是已经从recyclerview移除了
                footerView!!.post { addFooterView() }
            } else {
                adapter.addFooter(footerView!!)
            }
        }

        private fun findLastVisiblePosition(recyclerView: RecyclerView): Int {
            return when (val manager = recyclerView.layoutManager) {
                is LinearLayoutManager -> {
                    manager.findLastVisibleItemPosition()
                }
                is StaggeredGridLayoutManager -> {
                    manager.findLastVisibleItemPositions(null)[0]
                }
                else -> {
                    -1
                }
            }
        }
    }

    fun enableLoadMore(callback: () -> Unit, prefetchSize: Int) {
        if (adapter !is HiAdapter) {
            HiLog.e("enableLoadMore must use hidapter")
            return
        }
        loadMoreListener = LoadMoreListener(prefetchSize, callback)
        addOnScrollListener(loadMoreListener!!)
    }

    fun disableLoadMore() {
        if (adapter !is HiAdapter) {
            HiLog.e("enableLoadMore must use hidapter")
            return
        }
        val hiAdapter = adapter as HiAdapter
        footerView.let {
            if (footerView!!.parent != null) {
                hiAdapter.removeFooter(it!!)
            }
        }
        loadMoreListener?.let {
            removeOnScrollListener(it)
            loadMoreListener=null
            footerView=null
            isLoadingMore=false
        }
    }

    fun isLoading(): Boolean {
        return isLoadingMore
    }

    fun loadFinished(){
        // TODO: 2021-05-29  
    }
}