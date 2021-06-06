package com.kailaisi.hiapp

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.google.gson.JsonObject
import com.kailaisi.common.ui.component.HiBaseActivity
import com.kailaisi.hiapp.databinding.ActivityMainBinding
import com.kailaisi.hiapp.http.ApiFactory
import com.kailaisi.hiapp.http.api.AccountApi
import com.kailaisi.hiapp.ui.login.MainActivityLogic
import com.kailaisi.library.restful.HiCallback
import com.kailaisi.library.restful.HiResponse
import com.kailaisi.library.util.HiDataBus

@Route(path = "/account/main")
class MainActivity : HiBaseActivity(), MainActivityLogic.ActivityProvider {
    lateinit var logic: MainActivityLogic
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        logic = MainActivityLogic(this,
            savedInstanceState)
        HiDataBus.with<String>("stickdata").setStickyData("stickydata from main")
        ApiFactory.create(AccountApi::class.java).login("imooc","123456").enqueue(object:HiCallback<String>{
            override fun onSuccess(response: HiResponse<String>) {

            }

            override fun onFailed(throwable: Throwable) {
            }

        })
    }

    /**
     * 防止压后台之后，再回来导致的fragment重叠
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        logic.onSaveInstanceState(outState)
    }
}