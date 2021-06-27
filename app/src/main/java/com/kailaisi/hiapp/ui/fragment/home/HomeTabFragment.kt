package com.kailaisi.hiapp.ui.fragment.home

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kailaisi.common.ui.component.HiAbsListFragment
import com.kailaisi.hi_ui.date_item.HiDataItem
import com.kailaisi.hiapp.http.ApiFactory
import com.kailaisi.hiapp.http.api.HomeApi
import com.kailaisi.hiapp.model.HomeModel
import com.kailaisi.library.restful.HiCallback
import com.kailaisi.library.restful.HiResponse
import com.kailaisi.library.restful.annotion.CacheStrategy

/**
 * 首页的tab对应的Fragment
 */
class HomeTabFragment : HiAbsListFragment() {
    private lateinit var viewModel: HomePageViewModel
    private var categoryId: String? = null
    val DEFAULT_TAB_ID = "1"

    companion object {
        fun newInstance(id: String): HomeTabFragment {
            val args = Bundle()
            val fragment = HomeTabFragment()
            args.putString("id", id)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onRefresh() {
        super.onRefresh()
        queryTabTCategoryList(CacheStrategy.CACHE_FIRST)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        categoryId = arguments?.getString("id", DEFAULT_TAB_ID)
        super.onViewCreated(view, savedInstanceState)
        viewModel=ViewModelProvider(this)[HomePageViewModel::class.java]
        queryTabTCategoryList(CacheStrategy.CACHE_FIRST)
        enableLoadMore {
            queryTabTCategoryList(CacheStrategy.NET_ONLY)
        }
    }


    override fun createLayoutManager(): RecyclerView.LayoutManager {
        var hot = TextUtils.equals(categoryId, DEFAULT_TAB_ID)
        return if (hot) super.createLayoutManager() else GridLayoutManager(context, 2)
    }

    private fun queryTabTCategoryList(cacheStrategy: Int) {
        viewModel.queryTabCategoryList(categoryId,pageIndex,cacheStrategy).observe(viewLifecycleOwner, Observer {
            if (it!=null){
                updateUi(it)
            }else{
                finishRefresh(null)
            }
        })
    }

    private fun updateUi(data: HomeModel) {
        if (isAlive) {
            val dataItems = mutableListOf<HiDataItem<*, *>>()
            data.bannerList?.let {
                dataItems.add(BannerItem(it))
            }
            data.subcategoryList?.let {
                dataItems.add(GridItem(it))
            }
            data.goodsList?.forEachIndexed { index, goodsModel ->
                dataItems.add(GoodsItem(goodsModel))
            }
        }
    }
}