package com.kailaisi.library.restful

/**
 * 描述：响应报文
 * 作者：wujinxiang
 * 创建时间：2021/6/1 10:30 AM
 */
open class HiResponse<T> {
    companion object {
        val SUCCESS = 0
    }

    var rawData: String? = null//原始数据
    var code = 0//业务状态码
    var data: T? = null//业务数据
    var errorData: Map<String, String>? = null//错误状态下的数据
    var msg: String? = null//错误信息
}
