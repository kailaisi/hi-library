package com.kailaisi.hi_ui.banner;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kailaisi.hi_ui.banner.core.HiIndicator;

/**
 * 默认的圆形指示器
 */
public class HiCircleIndicator extends FrameLayout implements HiIndicator<FrameLayout> {

    public HiCircleIndicator(@NonNull Context context) {
        super(context);
    }

    public HiCircleIndicator(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HiCircleIndicator(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public FrameLayout get() {
        return this;
    }

    @Override
    public void onInflate(int count) {

    }

    @Override
    public void onPointChange(int current, int count) {

    }
}
