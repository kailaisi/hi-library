package com.kailaisi.library.restful

/**
 * 描述：响应报文
 * 作者：kailaisi
 * 创建时间：2021/6/1 10:30 AM
 */
open class HiResponse<T> {
    companion object {
        val CACHE_SUCCESS: Int=304
        val SUCCESS = 0
        val RC_HAS_ERROR = 5000
        val RC_ACCOUNT_INVALID = 5001
        val RC_PWD_ERROR = 5002
        val RC_NEED_LOGIN = 5003
        val RC_NOT_PURCHASED = 5004
        val RC_CHECK_SERVER_ERROR = 5005
        val RC_USER_NAME_EXISTS = 5006
        val RC_HTML_INVALID = 8001
        val RC_CONFIG_INVALID = 8002


        val RC_USER_FORBID = 6001
        val RC_AUTH_TOKEN_EXPIRED = 4030
        val RC_AUTH_TOKEN_INVALID = 4031
    }

    var rawData: String? = null//原始数据
    var code = 0//业务状态码
    var data: T? = null//业务数据
    var errorData: Map<String, String>? = null//错误状态下的数据
    var msg: String? = null//错误信息

    fun successful(): Boolean {
        return code == SUCCESS || code== CACHE_SUCCESS
    }
}
