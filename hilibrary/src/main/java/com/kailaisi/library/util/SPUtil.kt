package com.kailaisi.library.util

import android.content.Context
import android.content.SharedPreferences

/**
 * sp辅助类
 */
object SPUtil {
    val CACHE_FILE = "cache_file"

    fun putString(key: String, value: String) {
        getShared()?.apply {
            edit().putString(key, value).commit()
        }
    }

    fun getString(key: String): String? {
        return getShared()?.getString(key, null)
    }


    fun putBoolean(key: String, value: Boolean) {
        getShared()?.apply {
            edit().putBoolean(key, value).commit()
        }
    }

    fun getBoolean(key: String): Boolean {
        return getShared()?.getBoolean(key, false) ?: false
    }

    fun putInt(key: String, value: Int) {
        getShared()?.apply {
            edit().putInt(key, value).commit()
        }
    }

    fun getInt(key: String): Int {
        return getShared()?.getInt(key, 0) ?: 0
    }

    fun getShared(): SharedPreferences? {
        val application = AppGlobals.get()
        return application?.getSharedPreferences(CACHE_FILE, Context.MODE_PRIVATE)
    }
}