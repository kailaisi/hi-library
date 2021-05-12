package com.kailaisi.hiapp

import android.app.Application
import com.google.gson.Gson
import com.kailaisi.library.log.HiConsolePrinter
import com.kailaisi.library.log.HiLogConfig
import com.kailaisi.library.log.HiLogManager

/**
 * 描述：
 *
 * 作者：wu
 * <br></br>创建时间：2021-05-10:22:58
 */
class MyApplication : Application() {
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
    }
}