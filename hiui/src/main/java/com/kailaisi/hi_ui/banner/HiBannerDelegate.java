package com.kailaisi.hi_ui.banner;

import android.content.Context;
import android.util.SparseArray;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.FrameLayout;

import androidx.viewpager.widget.ViewPager;

import com.kailaisi.hi_ui.R;
import com.kailaisi.hi_ui.banner.core.HiBannerAdapter;
import com.kailaisi.hi_ui.banner.core.HiBannerMo;
import com.kailaisi.hi_ui.banner.core.HiIndicator;
import com.kailaisi.hi_ui.banner.core.IBindAdapter;
import com.kailaisi.hi_ui.banner.core.IHiBanner;
import com.kailaisi.library.log.HiViewPrinter;

import org.jetbrains.annotations.NotNull;

import java.io.FileFilter;
import java.util.List;

/**
 * HiBanner的控制器
 * 将HiBanner的内部逻辑抽取在这个类中，保证HiBanner类的整洁
 */
public class HiBannerDelegate implements IHiBanner, ViewPager.OnPageChangeListener {
    private Context mContext;
    private HiBanner mHiBanner;
    private IHiBanner.OnBannerClickListener mBannerClickListener;
    private ViewPager.OnPageChangeListener mOnPageChangeListener;
    private HiBannerAdapter mAdapter;
    private HiIndicator<?> mHiIndicator;
    private HiViewPager mHiViewPager;
    private List<? extends HiBannerMo> models;
    private int mIntervalTime = 5000;
    /**
     * 是否启动自动轮播
     */
    private boolean mAutoPlay = true;
    //在非自动轮播状态下是否可以循环切换
    private boolean mLoop = false;

    public HiBannerDelegate(Context context, HiBanner hiBanner) {
        this.mContext = context;
        mHiBanner = hiBanner;
    }

    @Override
    public void setBannerData(int layoutId, @NotNull List<? extends HiBannerMo> models) {
        this.models = models;
        init(layoutId);
    }


    @Override
    public void setBannerData(@NotNull List<? extends HiBannerMo> models) {
        setBannerData(R.layout.hi_bannere_item_image, models);
    }

    @Override
    public void setHaIndicator(HiIndicator<?> haIndicator) {
        this.mHiIndicator = haIndicator;
    }

    @Override
    public void setAutoPlay(boolean autoPlay) {
        this.mAutoPlay = autoPlay;
        if (mAdapter != null) mAdapter.setAutoPlay(autoPlay);
        if (mHiViewPager != null) mHiViewPager.setAutoPlay(autoPlay);

    }

    @Override
    public void setLoop(boolean loop) {
        this.mLoop = loop;
    }

    @Override
    public void setIntervalTime(int intervalTime) {
        if (intervalTime > 0) {
            this.mIntervalTime = intervalTime;
        }
    }

    @Override
    public void setBindAdapter(IBindAdapter bindAdapter) {
        mAdapter.setBindAdapter(bindAdapter);
    }

    @Override
    public void setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
        this.mOnPageChangeListener = onPageChangeListener;
    }

    @Override
    public void setOnBannerClickListener(OnBannerClickListener onBannerClickListener) {
        this.mBannerClickListener = onBannerClickListener;
    }


    private void init(int layoutId) {
        if (mAdapter == null) {
            mAdapter = new HiBannerAdapter(mContext);
        }
        if (mHiIndicator == null) {
            mHiIndicator = new HiCircleIndicator(mContext);
        }
        mHiIndicator.onInflate(layoutId);
        mAdapter.setLayoutResId(layoutId);
        mAdapter.setBannerData(models);
        mAdapter.setAutoPlay(mAutoPlay);
        mAdapter.setLoop(mLoop);
        mAdapter.setBannerClickListener(mBannerClickListener);
        mHiViewPager = new HiViewPager(mContext);
        mHiViewPager.setIntervalTime(mIntervalTime);
        mHiViewPager.addOnPageChangeListener(this);
        mHiViewPager.setAutoPlay(mAutoPlay);
        mHiViewPager.setAdapter(mAdapter);

        if ((mLoop || mAutoPlay) && mAdapter.getRealSize() != 0) {
            int firstItem = mAdapter.getFirstItem();
            mHiViewPager.setCurrentItem(firstItem, false);
        }
        //清除缓存
        mHiBanner.removeAllViews();
        mHiBanner.addView(mHiViewPager);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mHiBanner.addView(mHiIndicator.get(), params);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (null != mOnPageChangeListener && mAdapter.getRealSize() != 0) {
            mOnPageChangeListener.onPageScrolled(position % mAdapter.getRealSize(), positionOffset, positionOffsetPixels);
        }
    }

    @Override
    public void onPageSelected(int position) {
        if (mAdapter.getRealSize() == 0) {
            return;
        }
        position = position % mAdapter.getRealSize();
        if (null != mOnPageChangeListener && mAdapter.getRealSize() != 0) {
            mOnPageChangeListener.onPageSelected(position);
        }
        if (mHiIndicator != null) {
            mHiIndicator.onPointChange(position, mAdapter.getRealSize());
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (null != mOnPageChangeListener)
            mOnPageChangeListener.onPageScrollStateChanged(state);
    }
}