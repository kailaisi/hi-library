package com.kailaisi.hiapp

import android.content.Context
import androidx.multidex.MultiDex
import com.alibaba.android.arouter.launcher.ARouter
import com.google.gson.Gson
import com.kailaisi.common.ui.component.HiBaseApplication
import com.kailaisi.library.log.HiConsolePrinter
import com.kailaisi.library.log.HiLogConfig
import com.kailaisi.library.log.HiLogConfig.JsonParser
import com.kailaisi.library.log.HiLogManager
import com.tencent.bugly.Bugly
import com.tencent.bugly.beta.Beta

/**
 * 描述：
 *
 * 作者：kailaisi
 * <br></br>创建时间：2021-05-10:22:58
 */
class HiApplication : HiBaseApplication() {
    override fun onCreate() {
        super.onCreate()
        HiLogManager.init(object : HiLogConfig() {
            override fun getGlobalTag(): String {
                return "MyApplication"
            }

            override fun enable(): Boolean {
                return true
            }

            override fun injectParser(): JsonParser {
                return JsonParser { Gson().toJson(it) }
            }
        }, HiConsolePrinter())
        if (BuildConfig.DEBUG){
            ARouter.openLog()
            ARouter.openDebug()
        }
        ARouter.init(this)
        Bugly.init(this, "900029763", false)
        Bugly.setIsDevelopmentDevice(this,true)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        // you must install multiDex whatever tinker is installed!
        MultiDex.install(base)
        // 安装tinker
        Beta.installTinker()
    }
}