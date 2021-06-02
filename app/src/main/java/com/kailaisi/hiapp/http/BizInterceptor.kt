package com.kailaisi.hiapp.http

import com.kailaisi.library.log.HiLog
import com.kailaisi.library.restful.HiInterceptor

/**
 * 业务拦截器，能够自动增加header信息和数据
 */
class BizInterceptor : HiInterceptor {
    override fun intercept(chain: HiInterceptor.Chain): Boolean {
        if (chain.isRequestPeriod) {
            val request = chain.request()
            request.addHeader("auth-token", "fd")
        } else if (chain.response() != null) {
            HiLog.dt("BizInterceptor", chain.response()?.rawData)
        }
        return false

    }

}
