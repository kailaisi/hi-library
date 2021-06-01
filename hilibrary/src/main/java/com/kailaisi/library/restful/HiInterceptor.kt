package com.kailaisi.library.restful

/**
 * 拦截器
 */
interface HiInterceptor {
    /**
     * 链操作，能够进行拦截
     */
    fun intercept(chain: Chain): Boolean

    interface Chain {
        val isRequestPeriod: Boolean
            get() = false

        fun request(): HiRequest

        /**
         * 在发起网络请求前，是空的
         */
        fun response(): HiResponse<*>?
    }
}
