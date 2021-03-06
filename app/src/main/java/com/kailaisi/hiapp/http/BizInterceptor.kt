package com.kailaisi.hiapp.http

import com.kailaisi.hiapp.ui.account.AccountManager
import com.kailaisi.library.log.HiLog
import com.kailaisi.library.restful.HiInterceptor
import com.kailaisi.library.util.SPUtil

/**
 * 业务拦截器，能够自动增加header信息和数据
 */
class BizInterceptor : HiInterceptor {
    override fun intercept(chain: HiInterceptor.Chain): Boolean {
        if (chain.isRequestPeriod) {
            val request = chain.request()
            val pass =AccountManager.getBoardingPass()?:""
            request.addHeader("auth-token", "MTU5Mjg1MDg3NDcwNw==")
            request.addHeader("boarding-pass", pass)
        } else if (chain.response() != null) {
            HiLog.dt("BizInterceptor", chain.response()?.rawData)
        }
        return false

    }

}
