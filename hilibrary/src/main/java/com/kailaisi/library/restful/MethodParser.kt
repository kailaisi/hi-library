package com.kailaisi.library.restful

import com.kailaisi.library.restful.annotion.*
import java.lang.IllegalArgumentException
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class MethodParser(val baseUrl: String, method: Method) {

    private var cacheStrategy: Int = CacheStrategy.NET_ONLY
    private var httpMethod: Int? = null
    private var parameters = mutableMapOf<String, String>()
    private var domainUrl: String? = null
    private val heads = mutableMapOf<String, String>()
    private var formPost: Boolean = false
    private lateinit var relativeUrl: String
    private var returnType: Type? = null

    init {
        //进行注解
        parseMethodAnnotation(method)
        //解析返回值
        parseMethodReturnType(method)
    }

    private fun parseMethodReturnType(method: Method) {
        if (method.returnType != HiCall::class.java) {
            throw  IllegalArgumentException("method ${method.name} must be type of HiCall.class")
        }
        //范型返回参数
        val genericReturnType = method.genericReturnType
        if (genericReturnType is ParameterizedType) {
            val actualTypeArguments = genericReturnType.actualTypeArguments
            require(actualTypeArguments.size == 1) { "method ${method.name} can only has one  generic type" }
            returnType = actualTypeArguments[0]
        } else {
            throw IllegalArgumentException("method ${method.name} must have one generic type")
        }
    }


    private fun parseMethodParameters(method: Method, args: Array<Any>) {
        val parameterAnnotations = method.parameterAnnotations
        val size = parameterAnnotations.size
        val equal = size == args.size
        require(equal) {
            "arguments annotations count $size is not equal to args size:${args.size}"
        }
        for (index in args.indices) {
            val annotations = parameterAnnotations[index]
            require(annotations.size <= 1) {
                "field can only has one annotation :index=$index"
            }
            val value = args[index]
            require(isPrimitive(value)) {
                "eight basic types are supported for now,index=$index"
            }
            val annotation = annotations[0]
            if (annotation is Field) {
                parameters[annotation.value] = args[index].toString()
            } else if (annotation is Path) {
                //需要动态替换
                val replacement = value.toString()
                val replaceName = annotation.value
                if (replaceName != null && replacement != null) {
                    relativeUrl = relativeUrl.replace("{$replaceName}", replacement)
                }
            } else if (annotation is CacheStrategy) {
                cacheStrategy = value as Int
            } else {
                throw  IllegalArgumentException("cannot handle method annotation ${annotation.javaClass.name}")
            }

        }
    }

    private fun isPrimitive(value: Any): Boolean {
        if (value.javaClass == String::class.java) {
            return true
        }
        kotlin.runCatching {
            val field = value.javaClass.getField("TYPE")
            val clazz = field.get(null) as Class<*>
            if (clazz.isPrimitive) {
                return true
            }
        }
        return false
    }

    private fun parseMethodAnnotation(method: Method) {
        val annotations = method.annotations
        for (annotation in annotations) {
            if (annotation is GET) {
                relativeUrl = annotation.value
                httpMethod = HiRequest.METHOD.GET
            } else if (annotation is POST) {
                relativeUrl = annotation.value
                httpMethod = HiRequest.METHOD.GET
                formPost = annotation.formPost
            } else if (annotation is BaseUrl) {
                domainUrl = annotation.value
            } else if (annotation is Headers) {
                val headers = annotation.value
                for (head in headers) {
                    val indexOf = head.indexOf(":")
                    check(!(indexOf == 0 || indexOf == -1)) {
                        "@headers value must be in the form [key:value],but find $head "
                    }
                    val key = head.substring(0, indexOf)
                    val value = head.substring(indexOf + 1).trim()
                    heads[key] = value
                }
            } else if (annotation is CacheStrategy) {
                cacheStrategy = annotation.value
            } else {
                throw  IllegalArgumentException("cannot handle method annotation ${annotation.javaClass.name}")
            }
        }
        require(httpMethod == HiRequest.METHOD.POST || httpMethod == HiRequest.METHOD.GET) {
            "method must be get or post"
        }

        if (domainUrl == null) {
            domainUrl = baseUrl
        }
    }

    fun newRequest(method: Method, args: Array<out Any>?): HiRequest {
        val arguments = args as Array<Any>? ?: arrayOf()
        parseMethodParameters(method, arguments)
        val request = HiRequest()
        request.baseUrl = domainUrl
        request.returnType = returnType
        request.relativeUrl = relativeUrl
        request.headers = heads
        request.httpMethod = httpMethod
        request.parameters = parameters
        request.formPost = formPost
        request.cacheStrategy=cacheStrategy
        return request
    }


    companion object {
        fun parse(baseUrl: String, method: Method): MethodParser {
            return MethodParser(baseUrl, method)
        }
    }
}
