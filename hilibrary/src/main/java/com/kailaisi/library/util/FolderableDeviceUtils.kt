package com.kailaisi.library.util

import android.os.Build
import android.text.TextUtils

/**
 * 描述：关于折叠屏的相关处理方法
 * <p/>作者：wu
 * <br/>创建时间：2021-07-13:14:30
 */
object FolderableDeviceUtils {
    //对于是否折叠，没有固定的api提供，所以只能针对特定的机型去处理了
    fun isFold():Boolean{
        if (TextUtils.equals(Build.BRAND,"samsung") && TextUtils.equals(Build.DEVICE,"Galaxy Z Fold2")){
            return   HiDisplayUtils.getScreenWidth()!=1768
        }else if (TextUtils.equals(Build.BRAND,"huawei") && TextUtils.equals(Build.DEVICE,"Matex")){
            return   HiDisplayUtils.getScreenWidth()!=2200
        }else if (TextUtils.equals(Build.BRAND,"google") && TextUtils.equals(Build.DEVICE,"generic_x86")){
            return   HiDisplayUtils.getScreenWidth()!=1768
        }else {
            return true
        }
    }
}