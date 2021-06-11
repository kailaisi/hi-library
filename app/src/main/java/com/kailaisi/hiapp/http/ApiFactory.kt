package com.kailaisi.hiapp.http

import com.kailaisi.library.restful.HiRestful
import com.kailaisi.library.util.SPUtil

object ApiFactory {
    private val HttpBaseUrl = "https://api.devio.org/as/"
    private val HttpsBaseUrl = "https://api.devio.org/as/"
    private val KEY_DEGRADE_HTTP="degrade_http"
    private val degrade=SPUtil.getBoolean(KEY_DEGRADE_HTTP)
    private val baseUrl =if (degrade) HttpBaseUrl else HttpsBaseUrl
    private val hiRestful = HiRestful(baseUrl, RetrofitCallFactory(baseUrl))

    init {
        hiRestful.addInterceptor(BizInterceptor())
            .addInterceptor(LoggerInterceptor())
            .addInterceptor(HttpStatusInterceptor())
        SPUtil.putBoolean(KEY_DEGRADE_HTTP,false)
    }

    fun <T> create(service: Class<T>): T {
        return hiRestful.create(service)
    }
}