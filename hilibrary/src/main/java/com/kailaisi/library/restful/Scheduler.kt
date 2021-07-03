package com.kailaisi.library.restful

import com.kailaisi.library.cache.HiStorage
import com.kailaisi.library.executor.HiExecutor
import com.kailaisi.library.log.HiLog
import com.kailaisi.library.restful.annotion.CacheStrategy
import com.kailaisi.library.util.MainHandler

/**
 * 静态代理，通过代理CallFactory创建出来的call对象，实现对应的拦截器功能
 */
class Scheduler(
    private val callFactory: HiCall.Factory,
    val interceptors: MutableList<HiInterceptor>
) {
    /**
     * 执行请求信息下
     */
    fun newCall(request: HiRequest): HiCall<*> {
        val newCall: HiCall<*> = callFactory.newCall(request)
        return ProxyCall(newCall, request)
    }

    internal inner class ProxyCall<T>(val delegate: HiCall<T>, val request: HiRequest) : HiCall<T> {
        override fun execute(): HiResponse<T> {
            dispatchInterceptor(request, null)
            if (request.cacheStrategy == CacheStrategy.CACHE_FIRST) {
                val cacheResponse = readCache()
                if (cacheResponse.data != null) {
                    return cacheResponse
                }
            }
            val response = delegate.execute()
            saveCacheIfNeed(response)
            dispatchInterceptor(request, response)
            return response
        }


        override fun enqueue(callback: HiCallback<T>?) {
            dispatchInterceptor(request, null)
            if (request.cacheStrategy == CacheStrategy.CACHE_FIRST) {
                HiExecutor.execute(runnable = Runnable {
                    val cacheResponse = readCache()
                    if (cacheResponse.data != null) {
                        MainHandler.sendAtFrontOfQueue(Runnable {
                            callback?.onSuccess(cacheResponse)
                        })
                        HiLog.d("enqueue ,cache:${request.getCacheKey()}")
                    }
                })
            }
            delegate.enqueue(object : HiCallback<T> {

                override fun onSuccess(response: HiResponse<T>) {
                    dispatchInterceptor(request, response)
                    saveCacheIfNeed(response)
                    callback?.onSuccess(response)
                }

                override fun onFailed(throwable: Throwable) {
                    callback?.onFailed(throwable)
                }

            })
        }

        /*保存缓存数据信息*/
        private fun saveCacheIfNeed(response: HiResponse<T>) {
            if (request.cacheStrategy == CacheStrategy.CACHE_FIRST || request.cacheStrategy == CacheStrategy.NET_CACHE) {
                if (response.data != null) {
                    HiExecutor.execute(runnable = Runnable {
                        HiStorage.saveCache(request.getCacheKey(), response.data)
                    })
                }
            }
        }

        private fun readCache(): HiResponse<T> {
            //查询缓存，需要提供一个cache key
            //可以使用request  url+参数方式    当作key
            val cacheKey = request.getCacheKey()
            val cache = HiStorage.getCache<T>(cacheKey)
            val response = HiResponse<T>().apply {
                data = cache
                code = HiResponse.CACHE_SUCCESS
                msg = "缓存获取成功"
            }
            return response
        }

        private fun dispatchInterceptor(request: HiRequest, response: HiResponse<T>?) {
            Chain(request, response).dispatch()
        }

        internal inner class Chain(val request: HiRequest, val response: HiResponse<T>?) :
            HiInterceptor.Chain {
            //代表的是分发的第几个拦截器
            var callIndex = 0
            override val isRequestPeriod: Boolean
                get() = response == null

            override fun request(): HiRequest {
                return request
            }

            override fun response(): HiResponse<*>? {
                return response
            }

            fun dispatch() {
                val interceptor = interceptors[callIndex]
                val intercept = interceptor.intercept(this)
                callIndex++
                if (!intercept && callIndex < interceptors.size) {
                    dispatch()
                }
            }
        }
    }

}
