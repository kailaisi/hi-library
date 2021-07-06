package com.kailaisi.biz_search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kailaisi.common.http.ApiFactory
import com.kailaisi.library.cache.HiStorage
import com.kailaisi.library.executor.HiExecutor
import com.kailaisi.library.restful.HiCallback
import com.kailaisi.library.restful.HiResponse

class SearchViewModel : ViewModel() {

    var kewWords: MutableList<KeyWord>? = null
    var pageIndex = 1
    val MAX_HISTORY_SIZE = 10

    companion object {
        const val PAGE_SIZE = 20
        const val KEY_SEARCH_HISTORY = "key_search_history"
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

    fun saveHistory(keyWord: KeyWord?) {
        if (kewWords == null) {
            kewWords = mutableListOf<KeyWord>()
        }

        kewWords?.apply {
            if (this.contains(keyWord)) {
                remove(kewWords)
            }
            add(0, keyWord!!)
            if (this.size > MAX_HISTORY_SIZE) {
                dropLast(this.size - MAX_HISTORY_SIZE)
            }
        }
    }

    val goodsSearchLiveData = MutableLiveData<GoodsSearchList>()

    fun goodsSearch(it: String, reset: Boolean) {
        if (reset) {
            pageIndex = 1
        }
        ApiFactory.create(SearchApi::class.java).goodSearch(it, pageIndex, PAGE_SIZE)
            .enqueue(object : HiCallback<GoodsSearchList> {
                override fun onSuccess(response: HiResponse<GoodsSearchList>) {
                    goodsSearchLiveData.postValue(response.data)
                    pageIndex++
                }

                override fun onFailed(throwable: Throwable) {
                    goodsSearchLiveData.postValue(null)
                }
            })
    }

    fun queryLocalHistory(): MutableLiveData<List<KeyWord>?> {
        val liveData = MutableLiveData<List<KeyWord>?>()
        HiExecutor.execute(runnable = Runnable {
            kewWords = HiStorage.getCache<List<KeyWord>>(KEY_SEARCH_HISTORY)?.toMutableList()
            liveData.postValue(kewWords)
        })
        return liveData
    }

    fun clearHistory() {
        HiExecutor.execute(runnable = Runnable {
            HiStorage.deleteCache(KEY_SEARCH_HISTORY)
        })
    }
}