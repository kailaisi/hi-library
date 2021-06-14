package com.kailaisi.library.restful

/**
 * 描述：callback的回调
 * 作者：kailaisi
 * 创建时间：2021/5/31 5:28 PM
 */
interface HiCallback<T> {
    /**
     * 成功
     */
    fun onSuccess(response:HiResponse<T>)

    /**
     * 失败
     */
    fun onFailed(throwable: Throwable)
}