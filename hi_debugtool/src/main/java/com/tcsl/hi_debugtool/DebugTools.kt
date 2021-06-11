package com.tcsl.hi_debugtool

import android.content.Intent
import android.os.Process.killProcess
import android.os.Process.myPid
import com.kailaisi.library.util.AppGlobals
import com.kailaisi.library.util.SPUtil

class DebugTools {

    @HiDebug(name = "开启扫描")
    fun testScan() {

    }

    fun buildVersion(): String {
        return "构建版本：${BuildConfig.BUILD_TYPE}.${BuildConfig.BUILD_TYPE}"
    }

    fun buildTime(): String {
        return "构建版本：${BuildConfig.BUILD_TIME}"
    }

    fun buildEnvironment(): String {
        return "构建环境：${if (BuildConfig.DEBUG) "测试" else "生产"}"
    }

    @HiDebug(name = "一键开启Https降级", desc = "降级成Http，可以使用抓包工具明文抓包")
    fun degrade2Http() {
        SPUtil.putBoolean("degrade_http", true)
        val context = AppGlobals.get()?.applicationContext ?: return
        val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
        intent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        /*重新启动应用*/
        context.startActivity(intent)
        /*杀掉当前进程*/
        killProcess(myPid())
        return
    }
}