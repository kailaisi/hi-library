package com.kailaisi.hi_ui.slide

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable

/**
 * 所有的属性
 */
data class MenuItemAttr(
    val width: Int,
    val height: Int,
    val textColor: ColorStateList,
    val textSize: Int,
    val selectTextSize: Int,
    val backgroundColor: Int,
    val selectBackgroundColor: Int,
    val indicator: Drawable? = null,
)