package com.kailaisi.library.util

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat

/**
 * 描述：
 * <p/>作者：wu
 * <br/>创建时间：2021-06-17:21:05
 */
object HiRes {

    fun getString(@StringRes stringRes: Int): String {
        return context().resources.getString(stringRes)
    }

    fun getString(@StringRes id:Int, vararg formatArgs:Any?): String {
        return context().getString(id,formatArgs)
    }

    fun getColor(@ColorRes colorRes: Int): Int {
        return ContextCompat.getColor(context(),colorRes)
    }

    fun getDrawable(@DrawableRes id:Int): Drawable? {
        return ContextCompat.getDrawable(context(),id)
    }

    private fun context(): Context {
        return AppGlobals.get() as Context
    }

    fun getColorStateList(@ColorRes id: Int): ColorStateList? {
        return ContextCompat.getColorStateList(context(),id)
    }
}