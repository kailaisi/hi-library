package com.kailaisi.hi_debugtool

import android.content.Intent
import androidx.appcompat.app.AppCompatDelegate
import com.kailaisi.hi_ui.tab.bottom.HiViewUtil
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
        intent?.addCategory(Intent.CATEGORY_LAUNCHER)
        intent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
        /*重新启动应用*/
        context.startActivity(intent)
        /*杀掉当前进程*/
        android.os.Process.killProcess(android.os.Process.myPid());
        return
    }

    @HiDebug(name = "打开/关闭fps", desc = "打开后可以实时关闭fps")
    fun toggleFps() {
        //todo 需要整理
    }

    @HiDebug(name = "打开/关闭暗黑模式", desc = "可以调整暗黑模式")
    fun toggleTheme() {
        if (HiViewUtil.lightMode()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }
}