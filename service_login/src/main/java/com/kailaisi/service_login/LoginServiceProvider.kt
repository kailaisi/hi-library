package com.kailaisi.service_login

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.launcher.ARouter

/**
 * 描述：同一封装，对外暴漏的接口，
 * <p/>作者：wu
 * <br/>创建时间：2021-07-03:13:06
 */
object LoginServiceProvider {
    private val iLoginService =
        ARouter.getInstance().build("/login/service").navigation() as? ILoginService

    fun isLogin(): Boolean {
        return iLoginService?.isLogin() == true
    }

    fun getUserProfile(
        lifecycleOwner: LifecycleOwner?,
        observer: Observer<UserProfile>,
        onlyCache: Boolean
    ) {
        iLoginService?.getUserProfile(lifecycleOwner, observer, onlyCache)
    }

    fun login(context: Context?, observer: Observer<Boolean>) {
        iLoginService?.login(context, observer)
    }

    fun getBoardingPass(): String? {
        return iLoginService?.getBoardingPass()
    }

}