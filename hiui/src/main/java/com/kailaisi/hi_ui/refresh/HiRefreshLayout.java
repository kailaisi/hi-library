package com.kailaisi.hi_ui.refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 描述：下拉刷新的布局
 * <p/>作者：wu
 * <br/>创建时间：2021-05-17:21:47
 */
class HiRefreshLayout extends FrameLayout implements HiRefresh {

    private HiOverView.HiRefreshState mState;
    private GestureDetector mDetector;
    private HiRefreshListener mHiRefreshListener;
    private HiOverView mHiOverView;
    private int mLastY;
    private boolean disableRefreshScroll;
    public HiRefreshLayout(@NonNull Context context) {
        this(context,null);
    }

    public HiRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public HiRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setDisableRefreshScroll(boolean disable) {

    }

    @Override
    public void refreshFinished() {

    }

    @Override
    public void setRefreshListener(HiRefreshListener listener) {

    }

    @Override
    public void setRefreshOverView(HiOverView view) {

    }
}
