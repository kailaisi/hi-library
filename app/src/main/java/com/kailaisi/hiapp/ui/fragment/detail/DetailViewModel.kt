package com.kailaisi.hiapp.ui.fragment.detail

import androidx.lifecycle.*
import com.kailaisi.hiapp.http.ApiFactory
import com.kailaisi.hiapp.http.api.DetailApi
import com.kailaisi.hiapp.model.DetailModel
import com.kailaisi.library.restful.HiCallback
import com.kailaisi.library.restful.HiResponse
import java.lang.Exception

class DetailViewModel(val goodsId: String?) : ViewModel() {

    fun queryDetailData():LiveData<DetailModel?>{
        val page=MutableLiveData<DetailModel>()
        goodsId?.let {
            ApiFactory.create(DetailApi::class.java).queryDetail(goodsId!!).enqueue(object:HiCallback<DetailModel>{
                override fun onSuccess(response: HiResponse<DetailModel>) {
                    if (response.successful()){
                        page.postValue(response.data)
                    }
                }

                override fun onFailed(throwable: Throwable) {
                    page.postValue(null)
                }

            })


        }
        return page
    }


    companion object {
        private class DetailViewModelFactory(val goodsId: String?) :
            ViewModelProvider.NewInstanceFactory() {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                try {
                    val constructor = modelClass.getConstructor(String::class.java)
                    if (constructor != null) {
                        return constructor.newInstance(goodsId)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                return super.create(modelClass)
            }
        }

        fun get(goodsId: String?, viewModelStoreOwner: ViewModelStoreOwner): DetailViewModel {
            return ViewModelProvider(viewModelStoreOwner, DetailViewModelFactory(goodsId)).get(
                DetailViewModel::class.java)
        }
    }
}