package com.kailaisi.library.util

import android.app.Application

/**
 * APP获取类，通过反射获取对应的类信息
 */
object AppGlobals {
    private var application: Application? = null

    fun get(): Application? {
        if (application == null) {
            try {
                val clazz = Class.forName("android.app.ActivityThread")
                application = clazz.getMethod("currentApplication").invoke(null) as Application
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return application
    }
}