package com.kailaisi.library.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;

public class HiDisplayUtils {
    // 获取屏幕的宽度
    public static int getScreenWidth(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return metrics.widthPixels;
    }

    // 获取屏幕的高度
    public static int getScreenHeight(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return metrics.heightPixels;
    }

    public static int dip2px(Context context, float value) {
        return unit2px(context, value, TypedValue.COMPLEX_UNIT_DIP);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    public static int sp2px(Context context, float value) {
        return unit2px(context, value, TypedValue.COMPLEX_UNIT_SP);
    }
    public static int unit2px(Context context, float value, int unit) {
        return (int) TypedValue.applyDimension(unit, value, context.getResources().getDisplayMetrics());
    }
}

