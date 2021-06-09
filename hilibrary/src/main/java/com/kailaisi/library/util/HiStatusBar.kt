package com.kailaisi.library.util

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.WindowManager

/**
 * 状态栏
 */
object HiStatusBar {
    fun setStatusBar(
        activity: Activity,
        darkContent: Boolean,
        statusBarColor: Int = Color.WHITE,
        translucent: Boolean = false,
    ) {
        val window = activity.window
        val decorView = window.decorView
        var visibility = decorView.systemUiVisibility
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //添加Flag把状态栏设为可绘制模式
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            //FLAG_TRANSLUCENT_STATUS不能和请求绘制一起使用，否则会没有效果
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            //设置颜色必须搭配以上两个flag.FLAG_TRANSLUCENT_STATUS不能和请求绘制一起使用，否则会没有效果
            window.statusBarColor = statusBarColor
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //设置字体颜色
            visibility = if (darkContent) {
                //白底黑字
                visibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                //黑底白字
                visibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            }
        }
        /*如果是沉浸式效果*/
        if (translucent) {
            visibility = visibility or
                    /*页面全屏，但是信号等字体看不见了*/
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    /*添加该flag，恢复信号、时间等字体可见*/
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        }
        decorView.systemUiVisibility = visibility
    }
}