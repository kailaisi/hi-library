package com.kailaisi.hiapp.route

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.annotation.Interceptor
import com.alibaba.android.arouter.facade.callback.InterceptorCallback
import com.alibaba.android.arouter.facade.template.IInterceptor
import com.kailaisi.library.util.MainHandler
import com.kailaisi.pub_mod.RouterFlag
import com.kailaisi.service_login.LoginServiceProvider
import java.lang.RuntimeException

/**
 * 描述：业务拦截器
 * <p/>作者：kailaisi
 * <br/>创建时间：2021-06-06:13:44
 */
@Interceptor(priority = 9)
open class BizGlobalInterceptor : IInterceptor {
    private var context: Context? = null

    override fun init(context: Context?) {
        this.context = context
    }

    override fun process(postcard: Postcard?, callback: InterceptorCallback?) {
        val extra = postcard!!.extra
        postcard.timeout = 2
        callback?.onContinue(postcard)
        if (extra and (RouterFlag.FLAG_LOGIN) != 0) {
            //判断是否已经登陆了，如果未登录，则拦截
            callback?.onInterrupt(RuntimeException("need login"))
            loginIntercept(postcard, callback)
        } else {
            callback?.onContinue(postcard)
        }
        /*  else if ((extra and RouterFlag.FLAG_AUTHENTICATION) != 0) {
              showToast("请先认证")
          } else if ((extra and RouterFlag.FLAG_VIP) != 0) {
              showToast("请先开通VIP")
          }*/
    }

    /**
     * 登录拦截器
     */
    private fun loginIntercept(postcard: Postcard, callback: InterceptorCallback?) {
        MainHandler.post(Runnable {
            Toast.makeText(context, "请先登录", Toast.LENGTH_SHORT).show()
            if (LoginServiceProvider.isLogin()) {
                callback?.onContinue(postcard)
            } else {
                /*通过这种监听登录成功之后的信息，可以继续原来的业务了，而不会打断了*/
                LoginServiceProvider.login(context, Observer {
                    callback?.onContinue(postcard)
                })
            }
        })
    }
}