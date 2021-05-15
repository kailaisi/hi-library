package com.kailaisi.hi_ui.tab.top;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

/**
 * 顶部的嘻嘻
 *
 * @param <Color>
 */
public class HiTabTopInfo<Color> {
    public enum TabType {
        BITMAP, TEXT
    }

    //底部导航，持有的外部的fragment的类
    public Class<? extends Fragment> fragment;
    public String name;
    public Bitmap defaultBitmap;
    public Bitmap selectedBitmap;

    public Color defaultColor;
    public Color tintColor;
    public TabType tabType;

    public HiTabTopInfo(String name, @NonNull Bitmap defaultBitmap, Bitmap selectedBitmap) {
        this.name = name;
        this.defaultBitmap = defaultBitmap;
        this.selectedBitmap = selectedBitmap;
        tabType = TabType.BITMAP;
    }

    public HiTabTopInfo(String name, @NonNull Color defaultColor, Color tintColor) {
        this.name = name;
        this.defaultColor = defaultColor;
        this.tintColor = tintColor;
        this.tabType = TabType.TEXT;
    }
}
