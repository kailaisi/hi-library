package com.kailaisi.library.restful

import java.lang.reflect.Method
import java.lang.reflect.Proxy
import java.util.concurrent.ConcurrentHashMap

open class HiRestful constructor(val baseUrl: String, val callFactory: HiCall.Factory) {

    private var interceptors = mutableListOf<HiInterceptor>()

    private var methodService = ConcurrentHashMap<Method, MethodParser>()
    private var scheduler: Scheduler
    fun addInterceptor(interceptor: HiInterceptor) {
        interceptors.add(interceptor)
    }

    init {
        scheduler = Scheduler(callFactory, interceptors)
    }

    fun <T> create(service: Class<T>):T {
       return Proxy.newProxyInstance(service.classLoader,
            arrayOf(service)
        ) { proxy, method, args ->
            var get = methodService.get(method)
            if (get == null) {
                get = MethodParser(baseUrl, method, args)
                methodService.put(method, get)
            }
            //具体的请求信息
            val request = get.newRequest()
            //callFactory.newCall(request)
            scheduler.newCall(request)
        } as T
    }
}