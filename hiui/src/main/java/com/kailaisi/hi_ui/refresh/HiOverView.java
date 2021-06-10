package com.kailaisi.hi_ui.refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kailaisi.library.util.HiDisplayUtils;

/**
 * 描述：下拉刷新的头部视图，可以重载这个类来定义
 * <p/>作者：kailaisi
 * <br/>创建时间：2021-05-17:21:37
 */
public abstract class HiOverView extends FrameLayout {
    public enum HiRefreshState {
        STATE_INIT,//初始态
        STATE_VISIBLE,//Header展示状态
        STATE_REFRESH,//刷新状态
        STATE_OVER,//超出可刷新距离状态
        STATE_OVER_RELEASE//超出刷新位置松手后的状态
    }

    protected HiRefreshState mState = HiRefreshState.STATE_INIT;
    /**
     * 触发下拉刷新的最小高度
     */
    public int mPullRefreshHeight;
    /**
     * 最小阻尼
     */
    public float minDamp = 1.6f;
    /**
     * 最大阻尼
     */
    public float maxDamp = 2.2f;

    public HiOverView(@NonNull Context context) {
        super(context);
        preInit();
    }

    public HiOverView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        preInit();
    }

    public HiOverView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        preInit();
    }

    protected void preInit() {
        mPullRefreshHeight = HiDisplayUtils.dip2px(66);
        init();
    }


    public abstract void init();

    protected abstract void onScroll(int scrollY, int pullRefreshHeight);

    /**
     * 显示视图层
     */
    protected abstract void onVisible();

    /**
     * 超过Overlay，释放就会加载
     */
    public abstract void onOver();

    /**
     * 开始刷新
     */
    public abstract void onRefresh();

    /**
     * 加载完成
     */
    public abstract void onFinish();

    public void setState(HiRefreshState mState) {
        this.mState = mState;
    }

    public HiRefreshState getState() {
        return mState;
    }
}
