package com.kailaisi.hiapp.http

import com.kailaisi.library.restful.HiRestful

object ApiFactory {
    private val baseUrl = "https://api.devio.org/as/"
    private val hiRestful = HiRestful(baseUrl, RetrofitCallFactory(baseUrl))

    init {
        hiRestful.addInterceptor(BizInterceptor())
            .addInterceptor(LoggerInterceptor())
            .addInterceptor(HttpStatusInterceptor())
    }

    fun <T> create(service: Class<T>): T {
        return hiRestful.create(service)
    }
}