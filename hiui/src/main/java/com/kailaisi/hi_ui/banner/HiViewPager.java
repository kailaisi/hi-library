package com.kailaisi.hi_ui.banner;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.kailaisi.hi_ui.banner.core.HiBannerAdapter;
import com.kailaisi.hi_ui.banner.core.HiBannerScroller;

import java.lang.reflect.Field;

/**
 * 描述：自动翻页的ViewPager
 * <p/>作者：wu
 * <br/>创建时间：2021-05-24:21:13
 */
class HiViewPager extends ViewPager {
    private int mIntervalTime;
    /**
     * 是否自动轮播
     */
    private boolean mAutoPlay=true;
    private boolean isLayout;

    private Handler mHandler=new Handler();

    private Runnable mRunnable=new Runnable() {
        @Override
        public void run() {
            //切换到下一个
            next();
            mHandler.postDelayed(this,mIntervalTime);
        }
    };


    public HiViewPager(@NonNull Context context) {
        super(context);
    }

    public HiViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setAutoPlay(boolean autoPlay) {
        this.mAutoPlay = autoPlay;
        if (!mAutoPlay){
            mHandler.removeCallbacks(mRunnable);
        }
    }

    public void setIntervalTime(int intervalTime) {
        this.mIntervalTime = intervalTime;
    }

    public void setScrollDuration(int duration){
        try {
            //黑科技，由于ViewPager的滚动时间是通过mScroller来控制的，所有这里通过反射，将其替代为我们自己的scroller
            Field field=ViewPager.class.getDeclaredField("mScroller");
            field.setAccessible(true);
            field.set(this,new HiBannerScroller(getContext(),duration));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        isLayout=true;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (isLayout && getAdapter()!=null && getAdapter().getCount()>0){
            try {
                //解决当viewpager滑动上去再显示的时候，没有动画的问题
                Field mScroller=ViewPager.class.getDeclaredField("mFirstLayout");
                mScroller.setAccessible(true);
                mScroller.set(this,false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        start();
    }

    @Override
    protected void onDetachedFromWindow() {
        //解决卡在中间的问题。只有当前activity在销毁的时候，才调用父类方法，其他时候只是停止
        if (((Activity)getContext()).isFinishing()){
            super.onDetachedFromWindow();
        }
        stop();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                start();
                break;
            default:
                stop();

        }
        return super.onTouchEvent(ev);
    }

    private void start() {
        mHandler.removeCallbacksAndMessages(null);
        if (mAutoPlay){
            mHandler.postDelayed(mRunnable,mIntervalTime);
        }
    }


    /**
     * 设置要显示的item，并返回item的pos
     */
    private int next() {
        int next=-1;
        if (getAdapter()==null ||getAdapter().getCount()<=1){
            stop();
            return next;
        }
        next=getCurrentItem()+1;
        if (next>=getAdapter().getCount()){
            //获取第一个item的索引
            next=((HiBannerAdapter)getAdapter()).getFirstItem();
        }
        setCurrentItem(next,true);
        return next;
    }

    private void stop() {
        mHandler.removeCallbacksAndMessages(null);
    }
}
