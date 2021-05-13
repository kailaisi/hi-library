package com.tcsl.hi_ui.tab.common;

import androidx.annotation.NonNull;
import androidx.annotation.Px;

/**
 * 描述：Tab对外开放的接口
 * 作者：kailaisii
 * 创建时间：2021/5/13 5:29 PM
 */
public interface IHiTab<D> extends IHiTabLayout.OnTabSelectedListener<D> {
    void setHiTabInfo(@NonNull D data);
    void resetHeight(@Px int height);
}
