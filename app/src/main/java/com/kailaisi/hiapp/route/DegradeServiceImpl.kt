package com.kailaisi.hiapp.route

import android.content.Context
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.facade.service.DegradeService
import com.alibaba.android.arouter.launcher.ARouter

/**
 * 描述：全局的降级服务，当路由的时候，如果目标不存在，重定向到统一错误页
 * <p/>作者：kailaisi
 * <br/>创建时间：2021-06-06:14:02
 */
@Route(path = "/degrade/global/service")
class DegradeServiceImpl : DegradeService {
    override fun init(context: Context?) {
        ARouter.getInstance().build("/degrade/global/activity").greenChannel().navigation()
    }

    override fun onLost(context: Context?, postcard: Postcard?) {
    }
}