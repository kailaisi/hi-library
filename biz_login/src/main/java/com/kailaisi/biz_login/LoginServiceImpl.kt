package com.kailaisi.biz_login

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.facade.template.IProvider
import com.kailaisi.pub_mod.UserProfile
import com.kailaisi.service_login.ILoginService

/**
 * 描述：
 * <p/>作者：wu
 * <br/>创建时间：2021-07-03:12:53
 */

@Route(path = "/login/service")
class LoginServiceImpl : ILoginService, IProvider {
    override fun login(context: Context?, observer: Observer<Boolean>) {
        AccountManager.login(context, observer)
    }

    override fun isLogin(): Boolean {
        return AccountManager.isLogin()
    }

    override fun getUserProfile(
        lifecycleOwner: LifecycleOwner?,
        observer: Observer<UserProfile>,
        onlyCache: Boolean
    ) {
        AccountManager.getUserProfile(lifecycleOwner, observer, onlyCache)
    }

    override fun getBoardingPass(): String? {
        return AccountManager.getBoardingPass()
    }


    override fun init(context: Context?) {
    }
}