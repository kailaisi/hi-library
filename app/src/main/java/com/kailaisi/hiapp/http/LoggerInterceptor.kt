package com.kailaisi.hiapp.http

import com.google.gson.Gson
import com.kailaisi.library.restful.HiInterceptor

/**
 * 描述：日志拦截器
 * <p/>作者：kailaisi
 * <br/>创建时间：2021-06-06:13:44
 */
open class LoggerInterceptor : HiInterceptor {
    private var json = Gson()
    override fun intercept(chain: HiInterceptor.Chain): Boolean {
        val request = chain.request()
        val response = chain.response()
        if (!chain.isRequestPeriod) {
            val sb = StringBuilder()
            sb.append("url:").append(request.relativeUrl).append("\n")
                .append("request:").append(json.toJson(request.parameters)).append("\n")
                .append("response:").append(json.toJson(json.toJson(response)))

        }
        return false
    }
}