package com.kailaisi.biz_search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kailaisi.common.http.ApiFactory
import com.kailaisi.library.restful.HiCallback
import com.kailaisi.library.restful.HiResponse

class SearchViewModel : ViewModel() {
    var pageIndex=1
    companion object{
        const val PAGE_SIZE=20;
    }
    fun quickSearch(key: String): MutableLiveData<List<KeyWord>?> {
        val liveData = MutableLiveData<List<KeyWord>?>()
        ApiFactory.create(SearchApi::class.java).quickSearch(key)
            .enqueue(object : HiCallback<QuickSearchList> {
                override fun onSuccess(response: HiResponse<QuickSearchList>) {
                    liveData.postValue(response.data?.list)
                }

                override fun onFailed(throwable: Throwable) {
                    liveData.postValue(null)
                }
            })
        return liveData
    }

    fun saveHistory(it: KeyWord) {
        TODO("Not yet implemented")
    }

    val goodsSearchLiveData=MutableLiveData<GoodsSearchList>()

    fun goodsSearch(it: KeyWord) {
        ApiFactory.create(SearchApi::class.java).goodSearch(it.keyWord,pageIndex, PAGE_SIZE).enqueue(object :HiCallback<GoodsSearchList>{
            override fun onSuccess(response: HiResponse<GoodsSearchList>) {
                goodsSearchLiveData.postValue(response.data)
            }

            override fun onFailed(throwable: Throwable) {
                goodsSearchLiveData.postValue(null)
            }
        })
    }
}