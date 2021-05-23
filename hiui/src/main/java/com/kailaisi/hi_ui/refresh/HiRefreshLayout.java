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

import com.kailaisi.library.log.HiLog;

import java.lang.invoke.MethodHandleInfo;

/**
 * 描述：下拉刷新的布局
 * <p/>作者：wu
 * <br/>创建时间：2021-05-17:21:47
 */
public class HiRefreshLayout extends FrameLayout implements HiRefresh {
    private static final String TAG = "HiRefreshLayout";
    private HiOverView.HiRefreshState mState;
    private GestureDetector mDetector;
    private HiRefreshListener mHiRefreshListener;
    private HiOverView mHiOverView;
    private int mLastY;
    //刷新时是否禁止滚动
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
        disableRefreshScroll = disable;
    }

    @Override
    public void refreshFinished() {
        View head = getChildAt(0);
        mHiOverView.onFinish();
        mHiOverView.setState(HiOverView.HiRefreshState.STATE_INIT);
        int bottom = head.getBottom();
        if (bottom > 0) {
            recover(bottom);
        }
        mState = HiOverView.HiRefreshState.STATE_INIT;
    }

    @Override
    public void setRefreshListener(HiRefreshListener listener) {
        this.mHiRefreshListener = listener;
    }

    @Override
    public void setRefreshOverView(HiOverView view) {
        if (mHiOverView != null) {
            removeView(mHiOverView);
        }
        this.mHiOverView = view;
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        addView(mHiOverView, 0, params);
    }

    HiGestureDetector hiGestureDetector = new HiGestureDetector() {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (Math.abs(distanceX) > Math.abs(distanceY) || mHiRefreshListener != null && !mHiRefreshListener.enableRefresh()) {
                //横向滑动  或者刷新被禁止了。不处理
                return false;
            }
            if (disableRefreshScroll && mState == HiOverView.HiRefreshState.STATE_REFRESH) {
                //刷新时，而且设置了刷新时禁止滚动。则消费掉
                return true;
            }
            View head = getChildAt(0);
            View child = HiScrollUtil.findScrollableChild(HiRefreshLayout.this);
            if (HiScrollUtil.childScrolled(child)) {
                //如果列表发生了滚动则不处理
                return false;
            }
            //没有刷新或者没有达到可以刷新的距离，并且头部已经划出或者下拉
            if ((mState != HiOverView.HiRefreshState.STATE_REFRESH || head.getBottom() <= mHiOverView.mPullRefreshHeight) &&
                    (head.getBottom() > 0 || distanceY <= 0.0f)) {
                //如果还在滑动中
                if (mState != HiOverView.HiRefreshState.STATE_OVER_RELEASE) {
                    //计算速度。
                    int seed = 0;
                    if (child.getTop() < mHiOverView.mPullRefreshHeight) {
                        seed = (int) (mLastY / mHiOverView.minDamp);
                    } else {
                        seed = (int) (mLastY / mHiOverView.maxDamp);
                    }
                    //如果正在刷新，则不允许在滑动的时候改变状态
                    boolean bool = moveDown(seed, true);
                    mLastY = (int) -distanceY;
                    return bool;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
    };

    /**
     * 根据偏移量移动header与child
     *
     * @param offsetY 偏移量
     * @param nonAuto 是否非自动滚动触发
     */
    private boolean moveDown(int offsetY, boolean nonAuto) {
        HiLog.i("111", "changeState:" + nonAuto);
        View head = getChildAt(0);
        View child = getChildAt(1);
        int childTop = child.getTop() + offsetY;
        HiLog.i("-----", "moveDown head-bottom:" + head.getBottom() + ",child.getTop():" + child.getTop() + ",offsetY:" + offsetY);
        if (childTop <= 0) {
            HiLog.i(TAG, "childTop<=0,mState" + mState);
            offsetY = -childTop;//异常情况的补充
            //回到原始位置
            head.offsetTopAndBottom(offsetY);
            child.offsetTopAndBottom(offsetY);
            if (mState != HiOverView.HiRefreshState.STATE_REFRESH) {
                mState = HiOverView.HiRefreshState.STATE_INIT;
            }
        } else if (mState == HiOverView.HiRefreshState.STATE_REFRESH && childTop > mHiOverView.mPullRefreshHeight) {
            //正在下来刷新中，禁止继续下拉
            return false;
        } else if (childTop <= mHiOverView.mPullRefreshHeight) {//还没有超出设定的刷新距离
            if (mHiOverView.getState() != HiOverView.HiRefreshState.STATE_VISIBLE && nonAuto) {//头部显示出来了
                mHiOverView.onVisible();
                mHiOverView.setState(HiOverView.HiRefreshState.STATE_VISIBLE);
                mState = HiOverView.HiRefreshState.STATE_VISIBLE;
            }
            head.offsetTopAndBottom(offsetY);
            child.offsetTopAndBottom(offsetY);
            if (childTop == mHiOverView.mPullRefreshHeight && mState == HiOverView.HiRefreshState.STATE_OVER_RELEASE) {
                //下拉刷新完成
                HiLog.i(TAG, "refresh，childTop：" + childTop);
                refresh();
            }
        } else {
            if (mHiOverView.getState() != HiOverView.HiRefreshState.STATE_OVER && nonAuto) {
                //超出刷新位置
                mHiOverView.onOver();
                mHiOverView.setState(HiOverView.HiRefreshState.STATE_OVER);
            }
            head.offsetTopAndBottom(offsetY);
            child.offsetTopAndBottom(offsetY);
        }
        if (mHiOverView != null) {
            mHiOverView.onScroll(head.getBottom(), mHiOverView.mPullRefreshHeight);
        }
        return true;
    }

    /**
     *
     */
    private void refresh() {
        if (mHiRefreshListener != null) {
            mState = HiOverView.HiRefreshState.STATE_REFRESH;
            mHiOverView.onRefresh();
            mHiOverView.setState(HiOverView.HiRefreshState.STATE_REFRESH);
            mHiRefreshListener.onRefresh();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (!mAutoScroller.isIsFinished()) {
            return false;
        }
        //获取到下拉头
        View head = getChildAt(0);
        if (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_CANCEL
                || ev.getAction() == MotionEvent.ACTION_POINTER_INDEX_MASK) {
            //松开手，底部露出来了，需要恢复
            if (head.getBottom() > 0) {
                if (mState != HiOverView.HiRefreshState.STATE_REFRESH) {//非正在刷新
                    recover(head.getBottom());
                    //消费掉事件
                    return false;
                }
            }
            mLastY = 0;
        }
        //滚动过程交给gestureDetector来处理
        boolean consumed = mDetector.onTouchEvent(ev);
        if ((consumed || (mState != HiOverView.HiRefreshState.STATE_INIT && mState != HiOverView.HiRefreshState.STATE_REFRESH)) && head.getBottom() != 0) {
            ev.setAction(MotionEvent.ACTION_CANCEL);//骚操作。将ev设置为cancel。让父类接收不到真实的事件
            return super.dispatchTouchEvent(ev);
        }
        if (consumed) {
            //消费掉
            return true;
        } else {
            return super.dispatchTouchEvent(ev);
        }
    }

    /**
     * 恢复的距离
     */
    private void recover(int dis) {
        if (mHiRefreshListener != null && dis > mHiOverView.mPullRefreshHeight) {
            //先滚动到指定mHiOverView.mPullRefreshHeight。滚动距离为dis-mPullRefreshHeight
            //从这个位置开始触发下拉刷新
            mAutoScroller.recover(dis - mHiOverView.mPullRefreshHeight);
            //状态为超出下拉刷新
            mState = HiOverView.HiRefreshState.STATE_OVER_RELEASE;
        } else {
            //否则直接恢复
            mAutoScroller.recover(dis);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (getChildCount() < 2) {
            super.onLayout(changed, left, top, right, bottom);
        }
        //重新定义一下排版
        View head = getChildAt(0);
        HiLog.i(TAG, "onLayout head-height:" + head.getMeasuredHeight());
        View child = getChildAt(1);
        int childTop = child.getTop();
        if (mState == HiOverView.HiRefreshState.STATE_REFRESH) {
            head.layout(0, mHiOverView.mPullRefreshHeight - head.getMeasuredHeight(),
                    right, mHiOverView.mPullRefreshHeight);
            child.layout(0, mHiOverView.mPullRefreshHeight,
                    right, mHiOverView.mPullRefreshHeight + child.getMeasuredHeight());
        } else {
            head.layout(0, childTop - head.getMeasuredHeight(), right, childTop);
            child.layout(0, childTop, right, childTop + child.getMeasuredHeight());
        }
        View other;
        //让HiRefreshLayout节点下两个以上的child能够不跟随手势移动以实现一些特殊效果，如悬浮的效果
        for (int i = 2; i < getChildCount(); i++) {
            other = getChildAt(i);
            other.layout(0, top, right, bottom);
        }
        HiLog.i(TAG, "onLayout head-bottom:" + head.getBottom());
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
                moveDown(mLastY - mScroller.getCurrY(), false);
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
            //300ms恢复到dis位置dy：正直会向上滚动
            mScroller.startScroll(0, 0, 0, dis, 300);
            post(this);
        }

        boolean isIsFinished() {
            return mIsFinished;
        }
    }
}
