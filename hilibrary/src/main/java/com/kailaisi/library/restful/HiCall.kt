package com.kailaisi.library.restful

interface HiCall<T> {
    /**
     * 同步请求
     */
    fun execute(): HiResponse<T>

    /**
     * 异步请求
     */
    fun enqueue(callback: HiCallback<T>?)

    interface Factory{
        /**
         * HiCall的个公共场，用于创建HiCall对象
         */
        fun newCall(request:HiRequest):HiCall<*>
    }
}
