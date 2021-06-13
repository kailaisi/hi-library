package com.kailaisi.library.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import java.util.Objects;

public class HiDisplayUtils {
    // 获取屏幕的宽度
    public static int getScreenWidth() {
        DisplayMetrics metrics =AppGlobals.INSTANCE.get().getResources().getDisplayMetrics();
        return metrics.widthPixels;
    }

    // 获取屏幕的高度
    public static int getScreenHeight() {
        DisplayMetrics metrics = AppGlobals.INSTANCE.get().getResources().getDisplayMetrics();
        return metrics.heightPixels;
    }

    public static int dip2px(float value) {
        return unit2px(value, TypedValue.COMPLEX_UNIT_DIP);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    public static int sp2px( float value) {
        return unit2px(value, TypedValue.COMPLEX_UNIT_SP);
    }
    public static int unit2px( float value, int unit) {
        return (int) TypedValue.applyDimension(unit, value, Objects.requireNonNull(AppGlobals.INSTANCE.get()).getResources().getDisplayMetrics());
    }
}

