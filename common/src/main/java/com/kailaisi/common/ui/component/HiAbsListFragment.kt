package com.kailaisi.common.ui.component

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.core.widget.ContentLoadingProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kailaisi.common.R
import com.kailaisi.common.databinding.FragmentListBinding
import com.kailaisi.hi_ui.date_item.HiAdapter
import com.kailaisi.hi_ui.date_item.HiDataItem
import com.kailaisi.hi_ui.empty.EmptyView
import com.kailaisi.hi_ui.recyclerview.HiRecyclerView
import com.kailaisi.hi_ui.refresh.HiOverView
import com.kailaisi.hi_ui.refresh.HiRefresh
import com.kailaisi.hi_ui.refresh.HiRefreshLayout
import com.kailaisi.hi_ui.refresh.HiTextView

/**
 * 描述：通用的下拉刷新页面
 *
 * 作者：kailaisi
 * <br></br>创建时间：2021-05-30:17:19
 */
class HiAbsListFragment : HiBaseFragment(), HiRefresh.HiRefreshListener {
    companion object {
        private val PREFETCH_SIZE: Int = 5
    }

    private var pageIndex = 1
    private lateinit var hiAdapter: HiAdapter
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private var refreshLayout: HiRefreshLayout? = null
    private var loadingView: ContentLoadingProgressBar? = null
    private var emptyView: EmptyView? = null
    private var recyclerView: HiRecyclerView? = null
    private lateinit var refreshHeaderView: HiOverView
    override fun getLayoutId(): Int {
        return R.layout.fragment_list
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val inflate = FragmentListBinding.inflate(layoutInflater)
        this.refreshLayout = inflate.refreshLayout
        loadingView = inflate.contentLoading
        recyclerView = inflate.rvView
        emptyView = inflate.emptyView

        refreshHeaderView = HiTextView(requireContext())
        refreshLayout?.setRefreshOverView(refreshHeaderView)
        refreshLayout?.setRefreshListener(this)

        layoutManager = createLayoutManager()

        hiAdapter = HiAdapter(requireContext())
        recyclerView?.layoutManager = layoutManager
        recyclerView?.adapter = hiAdapter
        emptyView?.visibility = View.GONE
        emptyView?.setIcon(R.string.list_empty)
        emptyView?.setDesc(getString(R.string.list_empty_desc))
        emptyView?.setButton(getString(R.string.list_empty_action)) {
            onRefresh()
        }
        loadingView?.visibility = View.VISIBLE
        pageIndex = 1
    }

    fun finishRefresh(dataitem: List<HiDataItem<*, RecyclerView.ViewHolder>>?) {
        val success = !dataitem.isNullOrEmpty()
        //判断当前是否是下拉刷新（也可能存在下拉刷新的时候，执行了上拉分页），其实只是使用refresh是不可以的，还需要其他的措施来判断。
        //所以需要在enableLoadMore中处理掉
        val refresh = pageIndex == 1
        if (refresh) {
            loadingView?.visibility = View.GONE
            refreshLayout?.refreshFinished()
            if (success) {//刷新成功
                emptyView?.visibility = View.GONE
                hiAdapter.clearItems()
                hiAdapter.addItems(dataitem!!, true)
            } else {
                //判断当前列表是否有数据，如果没有，则显示空页面
                if (hiAdapter.itemCount <= 0) {
                    emptyView?.visibility = View.VISIBLE
                }
            }
        } else {
            if (success) {//刷新成功
                hiAdapter.addItems(dataitem!!, true)
            }
            recyclerView?.loadFinished(success)
        }
    }

    fun enableLoadMore(callback: () -> Unit) {
        recyclerView?.enableLoadMore({
            if (refreshHeaderView.state == HiOverView.HiRefreshState.STATE_REFRESH) {
                recyclerView?.loadFinished(false)
                return@enableLoadMore
            }
            pageIndex++
            callback.invoke()
        }, PREFETCH_SIZE)
    }

    fun disableLoadMore() {
        recyclerView?.disableLoadMore()
    }

    open fun createLayoutManager(): RecyclerView.LayoutManager {
        return LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    @CallSuper
    override fun onRefresh() {
        if (recyclerView?.isLoadingMore() == true) {
            //如果当前正在加载更多，则不允许进行下拉刷新
            refreshLayout?.post {
                refreshLayout?.refreshFinished()
            }
            return
        }
        pageIndex = 1
    }

    override fun enableRefresh(): Boolean {
        return true
    }
}