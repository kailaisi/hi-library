package com.kailaisi.library.util

import androidx.annotation.ColorRes

/**
 * 描述：
 * <p/>作者：wu
 * <br/>创建时间：2021-06-17:21:05
 */
object HiRes {
    fun getColor(@ColorRes colorRes: Int): Int {
      return  AppGlobals.get()!!.resources.getColor(colorRes)
    }
}