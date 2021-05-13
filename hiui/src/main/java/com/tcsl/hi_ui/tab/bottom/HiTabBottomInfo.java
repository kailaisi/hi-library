package com.tcsl.hi_ui.tab.bottom;

import android.graphics.Bitmap;

import androidx.fragment.app.Fragment;

public class HiTabBottomInfo<Color> {
    public enum TabType {
        BITMAP, ICON
    }
    //底部导航，持有的外部的fragment的类
    public Class<? extends Fragment> fragment;
    public String name;
    public Bitmap defaultBitmap;
    public Bitmap selectedBitmap;
    public String iconFont;
    public String defaultIconName;
    public String selectIconName;

    public Color defaultColor;
    public Color tintColor;
    public TabType tabType;

    public HiTabBottomInfo(String name, Bitmap defaultBitmap, Bitmap selectedBitmap) {
        this.name = name;
        this.defaultBitmap = defaultBitmap;
        this.selectedBitmap = selectedBitmap;
    }

    public HiTabBottomInfo(String name, String iconFont, String defaultIconName, String selectIconName, Color defaultColor, Color tintColor, TabType tabType) {
        this.name = name;
        this.iconFont = iconFont;
        this.defaultIconName = defaultIconName;
        this.selectIconName = selectIconName;
        this.defaultColor = defaultColor;
        this.tintColor = tintColor;
        this.tabType = tabType;
    }
}
