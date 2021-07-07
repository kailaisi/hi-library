package com.kailaisi.biz_order

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kailaisi.biz_order.address.Address
import com.kailaisi.biz_order.address.AddressApi
import com.kailaisi.common.http.ApiFactory

/**
 * 描述：
 * <p/>作者：wu
 * <br/>创建时间：2021-07-07:21:43
 */
class OrderViewModel: ViewModel() {

    fun queryMainAddress():LiveData<Address>{
        val liveData=MutableLiveData<Address>()
        ApiFactory.create(AddressApi::class.java)
        return liveData
    }
}