package com.kailaisi.hiapp.route

import android.content.Context
import android.widget.Toast
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.callback.InterceptorCallback
import com.alibaba.android.arouter.facade.template.IInterceptor
import java.lang.RuntimeException

/**
 * 描述：业务拦截器
 * <p/>作者：wu
 * <br/>创建时间：2021-06-06:13:44
 */
class BizInterceptor : IInterceptor {
    private var context: Context? = null

    override fun init(context: Context?) {
        this.context = context
    }

    override fun process(postcard: Postcard?, callback: InterceptorCallback?) {
        val extra = postcard!!.extra
        if (extra and (RouterFlag.FLAG_LOGIN) != 0) {
            //判断是否已经登陆了，如果未登录，则拦截
            callback?.onInterrupt(RuntimeException("need login"))
            showToast("请先登录")
        } else if ((extra and RouterFlag.FLAG_AUTHENTICATION) != 0) {
            showToast("请先认证")
        } else if ((extra and RouterFlag.FLAG_VIP) != 0) {
            showToast("请先开通VIP")
        }
    }

    private fun showToast(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }
}