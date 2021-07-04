package com.kailaisi.service_login

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer

/**
 * 描述：登录页面的对外暴漏的接口
 * <p/>作者：wu
 * <br/>创建时间：2021-07-03:12:49
 */
interface ILoginService {
    fun login(context:Context?,observer: Observer<Boolean>)

    fun isLogin():Boolean

    fun getUserProfile(lifecycleOwner: LifecycleOwner?,
                       observer: Observer<UserProfile>,
                       onlyCache:Boolean=false)

    fun getBoardingPass(): String?
}