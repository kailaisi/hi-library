package com.kailaisi.hi_ui.refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.Scroller;

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
    private AutoScroller mAutoScroller;

    public HiRefreshLayout(@NonNull Context context) {
        this(context, null);
    }

    public HiRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HiRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mDetector = new GestureDetector(getContext(), hiGestureDetector);
        mAutoScroller = new AutoScroller();
    }

    @Override
    public void setDisableRefreshScroll(boolean disable) {

    }

    @Override
    public void refreshFinished() {

    }

    @Override
    public void setRefreshListener(HiRefreshListener listener) {
        this.mHiRefreshListener = listener;
    }

    @Override
    public void setRefreshOverView(HiOverView view) {
        this.mHiOverView = view;
    }

    HiGestureDetector hiGestureDetector = new HiGestureDetector() {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (Math.abs(distanceX) > Math.abs(distanceY) || mHiRefreshListener != null && !mHiRefreshListener.enableRefresh()) {
                //横向滑动  或者刷新被禁止了。不处理
                return false;
            }
            if (disableRefreshScroll && mState == HiOverView.HiRefreshState.STATE_REFRESH) {
                //刷新时，而且设置了刷新时进制启动。则消费掉
                return true;
            }
            View head = getChildAt(0);
            View child = HiScrollUtil.findScrollableChild(HiRefreshLayout.this);
            if (HiScrollUtil.childScrolled(child)) {
                //如果列表发生了滚动则不处理
                return false;
            }
            if ((mState != HiOverView.HiRefreshState.STATE_REFRESH || head.getBottom() <= mHiOverView.mPullRefreshHeight) &&
                    (head.getBottom() > 0 || distanceY <= 0.0f)) {
                //如果还在滑动中
                if (mState != HiOverView.HiRefreshState.STATE_OVER_RELEASE) {
                    //计算速度
                    int seed = 0;
                    if (child.getTop() < mHiOverView.mPullRefreshHeight) {
                        seed = (int) (mLastY / mHiOverView.minDamp);
                    } else {
                        seed = (int) (mLastY / mHiOverView.maxDamp);
                    }
                    //如果正在刷新，则不允许在滑动的时候改变状态
                    boolean bool = moveDown(seed, true);
                }
            }
            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    };

    /**
     * 根据偏移量移动header与child
     *
     * @param offsetY 偏移量
     * @param nonAuto 是否非自动滚动触发
     */
    private boolean moveDown(int offsetY, boolean nonAuto) {
        View head = getChildAt(0);
        View child = getChildAt(1);
        int childTop = child.getTop() + offsetY;
        if (childTop <= 0) {
            offsetY = -childTop;
            head.offsetTopAndBottom(offsetY);
            child.offsetTopAndBottom(offsetY);
            if (mState != HiOverView.HiRefreshState.STATE_REFRESH) {
                mState = HiOverView.HiRefreshState.STATE_INIT;
            }
        } else if (mState == HiOverView.HiRefreshState.STATE_REFRESH && childTop > mHiOverView.mPullRefreshHeight) {
            //正在下来刷新中，禁止继续下拉
            return false;
        }
        return false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View head = getChildAt(0);
        if (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_CANCEL || ev.getAction() == MotionEvent.ACTION_POINTER_INDEX_MASK) {
            //松开手
            if (head.getBottom() > 0) {
                if (mState != HiOverView.HiRefreshState.STATE_REFRESH) {//松手，恢复
                    recover(head.getBottom());
                    return false;
                }
            }
            mLastY = 0;
        }
        //滚动过程交给gestureDetector来处理
        boolean consumed = mDetector.onTouchEvent(ev);
        if (consumed || mState != HiOverView.HiRefreshState.STATE_INIT && mState != HiOverView.HiRefreshState.STATE_REFRESH && head.getBottom() != 0) {
            ev.setAction(MotionEvent.ACTION_CANCEL);//让父类接收不到真实的事件
            return super.dispatchTouchEvent(ev);
        }
        if (consumed) {
            return true;
        } else {
            return super.dispatchTouchEvent(ev);
        }
    }

    /**
     * 恢复
     */
    private void recover(int dis) {
        if (mHiRefreshListener != null && dis > mHiOverView.mPullRefreshHeight) {
            //滚动到指定位置dis-mHiOverView.mPullRefreshHeight
            mAutoScroller.recover(dis - mHiOverView.mPullRefreshHeight);
            mState = HiOverView.HiRefreshState.STATE_OVER_RELEASE;
        } else {
            mAutoScroller.recover(dis);
        }
    }

    /**
     * 借助Scroller实现视图的自动滚动
     */
    private class AutoScroller implements Runnable {

        private Scroller mScroller;
        private int mLastY;
        private boolean mIsFinished;

        public AutoScroller() {
            mScroller = new Scroller(getContext(), new LinearInterpolator());
            mIsFinished = true;
        }

        @Override
        public void run() {
            if (mScroller.computeScrollOffset()) {
                //还未滚动完成
                mLastY = mScroller.getCurrY();
                post(this);
            } else {
                removeCallbacks(this);
                mIsFinished = true;
            }

        }


        void recover(int dis) {
            if (dis <= 0) {
                return;
            }
            removeCallbacks(this);
            mLastY = 0;
            mIsFinished = false;
            mScroller.startScroll(0, 0, 0, dis, 300);
            post(this);
        }

        boolean isIsFinished() {
            return mIsFinished;
        }
    }
}
