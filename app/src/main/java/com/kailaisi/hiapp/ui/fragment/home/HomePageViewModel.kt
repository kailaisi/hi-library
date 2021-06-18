package com.kailaisi.hiapp.ui.fragment.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.kailaisi.hiapp.http.ApiFactory
import com.kailaisi.hiapp.http.api.HomeApi
import com.kailaisi.hiapp.model.HomeModel
import com.kailaisi.hiapp.model.TabCategory
import com.kailaisi.library.restful.HiCallback
import com.kailaisi.library.restful.HiResponse
import com.kailaisi.library.restful.annotion.CacheStrategy

/**
 * 描述：
 * <p/>作者：wu
 * <br/>创建时间：2021-06-14:20:11
 */
class HomePageViewModel(private val savedState: SavedStateHandle) : ViewModel() {

    fun queryTabList(): MutableLiveData<List<TabCategory>?> {
        val liveData = MutableLiveData<List<TabCategory>?>()
        val memCache = savedState.get<List<TabCategory>?>("categoryTabs")
        if (memCache != null) {
            liveData.postValue(memCache)
            return liveData
        }
        ApiFactory.create(HomeApi::class.java).queryTabList()
            .enqueue(object : HiCallback<List<TabCategory>> {
                override fun onSuccess(response: HiResponse<List<TabCategory>>) {
                    val data = response.data
                    if (response.successful() && data != null) {
                        liveData.value = data
                        savedState.set("categoryTabs", data)
                    }
                }

                override fun onFailed(throwable: Throwable) {
                }

            })
        return liveData
    }

    fun queryTabCategoryList(
        categoryId: String?,
        pageIndex: Int,
        cacheStrategy: Int
    ): LiveData<HomeModel?> {
        val liveData = MutableLiveData<HomeModel?>()

        val memCache = savedState.get<HomeModel>("categoryList")
        //只有是第一次加载时  才需要从内存中取
        if (memCache != null && pageIndex == 1 && cacheStrategy == CacheStrategy.CACHE_FIRST) {
            liveData.postValue(memCache)
            return liveData
        }
        ApiFactory.create(HomeApi::class.java)
            .queryTabCategoryList( categoryId!!,cacheStrategy, pageIndex, 10)
            .enqueue(object : HiCallback<HomeModel> {
                override fun onSuccess(response: HiResponse<HomeModel>) {
                    val data = response.data;
                    if (response.successful() && data != null) {
                        //一次缓存数据，一次接口数据
                        liveData.value = data
                        //只有在刷新的时候，且不是本地缓存的数据 才存储到内容中
                        if (cacheStrategy != CacheStrategy.NET_ONLY && response.code == HiResponse.SUCCESS) {
                            savedState.set("categoryList", data)
                        }
                    } else {
                        liveData.postValue(null)
                    }
                }

                override fun onFailed(throwable: Throwable) {
                    liveData.postValue(null)
                }
            })

        return liveData
    }
}