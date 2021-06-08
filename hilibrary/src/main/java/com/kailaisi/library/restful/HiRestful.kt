package com.kailaisi.library.restful

import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy
import java.util.concurrent.ConcurrentHashMap

open class HiRestful constructor(val baseUrl: String, val callFactory: HiCall.Factory) {

    private var interceptors = mutableListOf<HiInterceptor>()

    private var methodService = ConcurrentHashMap<Method, MethodParser>()
    private var scheduler: Scheduler = Scheduler(callFactory, interceptors)
    fun addInterceptor(interceptor: HiInterceptor): HiRestful {
        interceptors.add(interceptor)
        return this
    }

    fun <T> create(service: Class<T>): T {
        return Proxy.newProxyInstance(service.classLoader,
            arrayOf(service), object : InvocationHandler {
                override fun invoke(proxy: Any, method: Method, args: Array<out Any>?): Any {
                    var methodParser = methodService.get(method)
                    if (methodParser == null) {
                        methodParser = MethodParser(baseUrl, method)
                        methodService[method] = methodParser
                    }
                    //具体的请求信息
                    val request = methodParser.newRequest(method, args)
                    //callFactory.newCall(request)
                    return scheduler.newCall(request)
                }
            }) as T
    }
}