package com.kailaisi.hi_ui.banner.core;

import android.view.View;

/**
 * 描述：
 * <p/>作者：kailaisi
 * <br/>创建时间：2021-05-24:21:50
 */
public interface HiIndicator<T extends View> {
    T get();

    /**
     * 初始化指示器
     * @param count
     */
    void onInflate(int count);

    /**
     * 幻灯片切换回调
     * @param current 切换到的幻灯片位置
     * @param count 幻灯片数量
     */
    void onPointChange(int current,int count);
}
