package com.kailaisi.biz_search

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.facade.annotation.Route
import com.kailaisi.common.ui.component.HiBaseActivity
import com.kailaisi.common.ui.view.EmptyView
import com.kailaisi.hi_ui.seach.HiSearchView
import com.kailaisi.library.util.HiDisplayUtils
import com.kailaisi.library.util.HiRes
import com.kailaisi.library.util.HiStatusBar
import kotlinx.android.synthetic.main.activity_search.*

/**
 * 描述：搜索页面
 * <p/>作者：wu
 * <br/>创建时间：2021-07-04:11:05
 */
@Route(path = "/search/main")
class SearchActivity : HiBaseActivity() {

    private var historyView: HistorySearchView? = null
    private var goodSearchView: GoodsSearchView? = null
    private var quickSearchView: QuickSearchView? = null
    private lateinit var viewModel: SearchViewModel
    private var emptyView: EmptyView? = null
    private var status: Int = -1
    private lateinit var searchView: HiSearchView
    private lateinit var searchButton: Button

    companion object {
        const val STATUS_EMPTY = 0
        const val STATUS_HISTORY = 1
        const val STATUS_QUICK_SEARCH = 2
        const val STATUS_GOODS_SEARCH = 3
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        HiStatusBar.setStatusBar(this, true, translucent = false)
        setContentView(R.layout.activity_search)
        viewModel = ViewModelProvider(this)[SearchViewModel::class.java]
        initTopBar()
        updateViewStatus(STATUS_EMPTY)
        //查询本地的历史搜索记录
        queryLocalHistory()
    }

    private fun queryLocalHistory() {
        viewModel.queryLocalHistory().observe(this, Observer {
            if (it != null) {
                updateViewStatus(STATUS_HISTORY)
                historyView?.bindData(it)
            }
        })
    }

    private fun initTopBar() {
        nav_bar.apply {
            setNavListener(View.OnClickListener {
                onBackPressed()
            })
            searchButton = addRightTextButton(R.string.nav_item_search, R.id.id_nav_item_search)
            searchButton.setTextColor(HiRes.getColorStateList(R.color.nav_item_search))
            searchButton.isEnabled = false
        }
        searchButton.setOnClickListener { searchClickListener }
        searchView = HiSearchView(this)
        searchView.layoutParams = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            HiDisplayUtils.dip2px(38f)
        )
        searchView.setHintText(HiRes.getString(R.string.search_hint))
        searchView.setClearIconClickListener(updateHistoryListener)
        searchView.setDebounceTextChangeListener { it ->
            val hasCount = !it.isNullOrBlank()
            if (hasCount) {
                searchButton.isEnabled = true
                viewModel.quickSearch(it!!).observe(this, Observer { list ->
                    if (list.isNullOrEmpty()) {
                        updateViewStatus(STATUS_EMPTY)
                    } else {
                        updateViewStatus(STATUS_QUICK_SEARCH)
                        quickSearchView?.bindData(list) {
                            doSearchWord(it)
                        }
                    }
                })
            }
        }
        nav_bar.setCenterView(searchView)
    }

    private fun doSearchWord(it: KeyWord) {
        //搜索框高亮，搜索词
        searchView.setKeyWord(it.keyWord, updateHistoryListener)
        //keyword存储起来
        viewModel.saveHistory(it)
        //发起搜索请求
        viewModel.goodsSearch(it.keyWord, true)
        val kwClearIconView = searchView.findViewById<View>(R.id.id_search_clear_icon)
        //在搜索的过程中，不允许清除keyword
        kwClearIconView?.isEnabled = false
        viewModel.goodsSearchLiveData.observe(this, Observer {
            kwClearIconView?.isEnabled = true
            goodSearchView?.loadFinished(it.list.isNullOrEmpty())
            val loadInit = viewModel.pageIndex == 1
            if (it == null) {
                if (loadInit) {
                    updateViewStatus(STATUS_EMPTY)
                }
            } else {
                updateViewStatus(STATUS_GOODS_SEARCH)
                goodSearchView?.bindData(it.list,loadInit)
            }
        })
    }

    private val updateHistoryListener = View.OnClickListener {
        if (viewModel.kewWords.isNullOrEmpty()) {
            updateViewStatus(STATUS_EMPTY)
        } else {
            updateViewStatus(STATUS_HISTORY)
            historyView?.bindData(viewModel.kewWords!!)
        }
    }

    private val searchClickListener = View.OnClickListener {
        val keyWord = searchView.editText?.text?.trim().toString()
        if (keyWord.isNotEmpty()) {
            doSearchWord(KeyWord(null, keyWord))
        }
    }

    private fun updateViewStatus(newStatus: Int) {
        if (status == newStatus) return
        status = newStatus

        var showView: View? = null
        when (status) {
            STATUS_EMPTY -> {
                if (emptyView == null) {
                    emptyView = EmptyView(this).apply {
                        setDesc(HiRes.getString(R.string.list_empty_desc))
                        setIcon(R.string.list_empty)
                    }
                }
                showView = emptyView
            }
            STATUS_HISTORY -> {
                if (historyView == null) {
                    historyView = HistorySearchView(this).apply {
                        setOnCheckedChangedListener {
                            doSearchWord(it)
                        }
                        setOnHistoryClearListener {
                            viewModel.clearHistory()
                            updateViewStatus(STATUS_EMPTY)
                        }
                    }
                }
            }
            STATUS_QUICK_SEARCH -> {
                if (quickSearchView == null) {
                    quickSearchView = QuickSearchView(this)
                }
                showView = quickSearchView
            }
            STATUS_GOODS_SEARCH -> {
                if (goodSearchView == null) {
                    goodSearchView = GoodsSearchView(this).apply {
                        enableLoadMore({
                            val keyWord = searchView.getKeyword()
                            if (keyWord.isNullOrBlank()) return@enableLoadMore
                            //分页
                            viewModel.goodsSearch(keyWord, false)
                        }, 5)
                    }
                    showView = quickSearchView
                }
            }
            else -> {
                throw IllegalAccessException("不支持的状态")
            }
        }

        if (showView != null) {
            if (showView.parent != null) {
                //如果没有添加过，则添加到父布局
                container.addView(showView)
            }
            val childCount = container.childCount
            for (index in 0 until childCount) {
                val child = container.getChildAt(index)
                if (child != showView) {
                    child.visibility = View.GONE
                } else {
                    child.visibility = View.VISIBLE
                }
            }
        }
    }
}