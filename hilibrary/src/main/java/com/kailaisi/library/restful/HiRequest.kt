package com.kailaisi.library.restful

import androidx.annotation.IntDef
import com.kailaisi.library.restful.annotion.CacheStrategy
import java.lang.IllegalArgumentException
import java.lang.reflect.Type
import java.net.URLEncoder

/**
 * 请求对象
 */
class HiRequest {
    var cacheStrategy: Int = CacheStrategy.NET_ONLY
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

    private var cacheStrategyKey: String? = null

    fun getCacheKey(): String {
        if (cacheStrategyKey.isNullOrBlank()) {
            val builder = StringBuilder()
            val endUrl = domainUrl()
            builder.append(endUrl)
            if (endUrl.indexOf("?") > 0 || endUrl.indexOf("&") > 0) {
                builder.append("&")
            } else {
                builder.append("?")
            }
            if (parameters != null) {
                parameters?.forEach {
                    builder.append(it.key).append("=").append(URLEncoder.encode(it.value, "utf-8"))
                }
                builder.deleteCharAt(builder.length - 1)
                cacheStrategyKey = builder.toString()
            } else {
                cacheStrategyKey = endUrl
            }
        }
        return cacheStrategyKey!!
    }

    @IntDef(value = [METHOD.GET, METHOD.POST])
    annotation class METHOD {
        companion object {
            const val GET = 0
            const val POST = 1
        }
    }
}
