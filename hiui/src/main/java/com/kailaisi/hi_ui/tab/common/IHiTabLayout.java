package com.kailaisi.hi_ui.tab.common;

import android.view.ViewGroup;

import androidx.annotation.NonNull;

import java.util.List;

/**
 * 描述：Tab对外开放的接口
 * 作者：kailaisii
 * 创建时间：2021/5/13 5:26 PM
 */
public interface IHiTabLayout<Tab extends ViewGroup, D> {
    Tab findTab(@NonNull D data);

    void addTabSelectedChangedListener(OnTabSelectedListener<D> listener);

    void defaultSelected(@NonNull D defaultInfo);

    void inflateInfo(@NonNull List<D> infoList);

    interface OnTabSelectedListener<D> {
        void onTabSelectedListener(int index, @NonNull D preInfo, @NonNull D nextInfo);
    }
}
