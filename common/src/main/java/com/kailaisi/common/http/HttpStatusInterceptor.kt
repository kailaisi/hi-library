package com.kailaisi.common.http

import com.alibaba.android.arouter.launcher.ARouter
import com.kailaisi.library.log.HiLog
import com.kailaisi.library.restful.HiInterceptor
import com.kailaisi.library.restful.HiResponse
import com.kailaisi.library.util.SPUtil

/**
 * 根据返回的status，自动路由到相关页面
 */
class HttpStatusInterceptor : HiInterceptor {
    override fun intercept(chain: HiInterceptor.Chain): Boolean {
        val res = chain.response()
        if (!chain.isRequestPeriod && res != null) {
            val code = res.code
            when (code) {
                HiResponse.RC_NEED_LOGIN -> ARouter.getInstance().build("/account/login")
                    .navigation()
                HiResponse.RC_AUTH_TOKEN_EXPIRED , HiResponse.RC_AUTH_TOKEN_INVALID , HiResponse.RC_USER_FORBID -> {
                    val url=res.errorData?.let {
                        it.get("helpUrl")
                    }
                    ARouter.getInstance()
                        .build("/degrade/global/activity")
                        .withString("degrade_title", "非法访问")
                        .withString("degrade_des", res.msg)
                        .withString("degrade_action", url)
                        .navigation()
                }
            }
        }
        return false

    }

}
