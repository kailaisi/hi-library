package com.kailaisi.library.restful

import androidx.annotation.IntDef
import java.lang.IllegalArgumentException
import java.lang.reflect.Type

/**
 * 请求对象
 */
class HiRequest {


    var formPost: Boolean = false

    @METHOD
    var httpMethod: Int? = null
    var parameters: MutableMap<String, String>? = null

    //域名地址
    var baseUrl: String? = null

    //相对路径
    var relativeUrl: String? = null
    var headers: MutableMap<String, String>? = null
    var returnType: Type? = null//restful方法的范型返回值

    /**
     * 获取全地址
     */
    fun domainUrl(): String {
        if (relativeUrl == null) {
            throw IllegalArgumentException("relative url must not be null")
        }
        if (!relativeUrl!!.startsWith("/")) {
            return baseUrl + relativeUrl
        }
        val indexOf = baseUrl!!.indexOf("/")
        return baseUrl!!.substring(0, indexOf) + relativeUrl
    }

    fun addHeader(key: String, value: String) {
        if (headers == null) {
            headers = mutableMapOf()
        }
        headers!![key] = value
    }

    @IntDef(value = [METHOD.GET, METHOD.POST])
    annotation class METHOD {
        companion object {
            const val GET = 0
            const val POST = 1
        }
    }
}
