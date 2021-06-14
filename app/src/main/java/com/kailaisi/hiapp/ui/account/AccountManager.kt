package com.kailaisi.hiapp.ui.account

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.kailaisi.hiapp.http.ApiFactory
import com.kailaisi.hiapp.http.api.AccountApi
import com.kailaisi.hiapp.model.UserProfile
import com.kailaisi.hiapp.ui.login.LoginActivity
import com.kailaisi.library.cache.HiStorage
import com.kailaisi.library.executor.HiExecutor
import com.kailaisi.library.restful.HiCallback
import com.kailaisi.library.restful.HiResponse
import com.kailaisi.library.util.AppGlobals
import com.kailaisi.library.util.SPUtil

/**
 * 描述：用户管理类，将登录、以及用户信息通过该类进行处理
 * <p/>作者：kailaisi
 * <br/>创建时间：2021-06-14:17:46
 */
object AccountManager {
    private var userProfile: UserProfile? = null
    private var boardingPass: String? = null
    private val KEY_BOARDING_PASS = "boarding_pass"
    private val loginForeverObservers = mutableListOf<Observer<Boolean>>()
    private val loginLiveData = MutableLiveData<Boolean>()
    private val profileLiveData = MutableLiveData<UserProfile>()
    private val profileForeverObservers = mutableListOf<Observer<UserProfile>>()

    @Volatile
    private var isFetching = false

    fun login(context: Context? = AppGlobals.get(), observer: Observer<Boolean>) {
        if (context is LifecycleOwner) {
            loginLiveData.observe(context, observer)
        } else {
            loginLiveData.observeForever(observer)
            loginForeverObservers.add(observer)
        }
        val intent = Intent(context, LoginActivity::class.java)
        if (context is Application) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        if (context == null) {
            throw  IllegalStateException("context must not be null")
        }
        context.startActivity(intent)
    }

    fun getBoardingPass(): String? {
        if (boardingPass.isNullOrBlank()) {
            boardingPass = SPUtil.getString(KEY_BOARDING_PASS)
        }
        return boardingPass
    }

    fun isLogin(): Boolean {
        return !getBoardingPass().isNullOrBlank()
    }

    fun loginSuccess(boarding: String) {
        SPUtil.putString(KEY_BOARDING_PASS, boarding)
        this.boardingPass = boarding
        loginLiveData.value = true
        clearLoginForever()
    }

    private fun clearLoginForever() {
        loginForeverObservers.forEach {
            loginLiveData.removeObserver(it)
        }
        loginForeverObservers.clear()
    }

    /*1.可能多线程在处理,在进行请求信息的时候，可能另一个正在进行请求处理，所以这里通过boolean来判断处理*/
    @Synchronized
    fun getUserProfile(
        lifecycleOwner: LifecycleOwner?,
        observer: Observer<UserProfile>,
        onlyCache: Boolean = true,/*因为我们做了缓存处理，所以我们需要通过某个字段来标识是否使用缓存，这样的话，就可以处理登录后无法再次获取用户信息的问题了*/
    ) {
        if (lifecycleOwner == null) {
            profileLiveData.observeForever(observer)
            profileForeverObservers.add(observer)
        } else {
            profileLiveData.observe(lifecycleOwner, observer)
        }
        if (userProfile != null && onlyCache) {
            profileLiveData.value = userProfile
            clearProfileForeverObservers()
            return
        }
        if (isFetching) return
        isFetching = true
        ApiFactory.create(AccountApi::class.java).profile()
            .enqueue(object : HiCallback<UserProfile> {
                override fun onSuccess(response: HiResponse<UserProfile>) {
                    userProfile = response.data
                    if (response.sucessful()) {
                        /*缓存用户信息信息*/
                        HiExecutor.execute(runnable = {
                            HiStorage.saveCache("user_profile", userProfile)
                            isFetching = false
                        })
                        /*postvalue是仍到handler中去处理，如果使用post，那么底部直接clear清楚了观察者，会导致部分观察者收不到数据，所以这里使用的是setvalue*/
                        profileLiveData.value = userProfile
                    } else {
                        isFetching = false
                        profileLiveData.value = null
                    }
                    clearProfileForeverObservers()
                }

                override fun onFailed(throwable: Throwable) {
                    isFetching = false
                    profileLiveData.value = null
                }

            })
    }

    private fun clearProfileForeverObservers() {
        profileForeverObservers.forEach {
            profileLiveData.removeObserver(it)
        }
        profileForeverObservers.clear()
    }
}