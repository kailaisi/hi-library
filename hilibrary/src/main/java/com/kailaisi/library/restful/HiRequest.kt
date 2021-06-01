package com.kailaisi.library.restful

import androidx.annotation.IntDef
import java.lang.reflect.Type

/**
 * 请求对象
 */
class HiRequest {
    @METHOD
    var httpMethod: Int? = null
    var parameters: MutableMap<String, Any>? = null

    //域名地址
    var baseUrl: String? = null

    //相对路径
    var relativeUrl: String? = null
    var headers: Map<String, String>? = null
    var returnType: Type? = null//restful方法的范型返回值

    @IntDef(value = [METHOD.GET, METHOD.POST])
    internal annotation class METHOD {
        companion object {
            const val GET = 0
            const val POST = 1
        }
    }
}
