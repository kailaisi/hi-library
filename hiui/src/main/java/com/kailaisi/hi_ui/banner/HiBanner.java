package com.kailaisi.hi_ui.banner;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.kailaisi.hi_ui.R;
import com.kailaisi.hi_ui.banner.core.HiBannerMo;
import com.kailaisi.hi_ui.banner.core.HiIndicator;
import com.kailaisi.hi_ui.banner.core.IBindAdapter;
import com.kailaisi.hi_ui.banner.core.IHiBanner;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * 最基础的Banner类
 */
public class HiBanner extends FrameLayout implements IHiBanner {

    private HiBannerDelegate delegate;

    public HiBanner(@NonNull Context context) {
        this(context, null);
    }

    public HiBanner(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HiBanner(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        delegate = new HiBannerDelegate(context, this);
        initAttrs(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.HiBanner);
        boolean autoPlay = array.getBoolean(R.styleable.HiBanner_autoPlay, true);
        boolean loop = array.getBoolean(R.styleable.HiBanner_loop, true);
        int integer = array.getInteger(R.styleable.HiBanner_intervalTime, -1);
        setAutoPlay(autoPlay);
        setLoop(loop);
        setIntervalTime(integer);
        array.recycle();
    }

    @Override
    public void setBannerData(int layoutId, @NotNull List<? extends HiBannerMo> models) {
        delegate.setBannerData(layoutId, models);
    }

    @Override
    public void setBannerData(@NotNull List<? extends HiBannerMo> models) {
        delegate.setBannerData(models);
    }

    @Override
    public void setHiIndicator(HiIndicator<?> hiIndicator) {
        delegate.setHiIndicator(hiIndicator);
    }

    @Override
    public void setAutoPlay(boolean autoPlay) {
        delegate.setAutoPlay(autoPlay);
    }

    @Override
    public void setLoop(boolean loop) {
        delegate.setLoop(loop);
    }

    @Override
    public void setIntervalTime(int intervalTime) {
        delegate.setIntervalTime(intervalTime);
    }

    @Override
    public void setScrollDuration(int duration) {
        delegate.setScrollDuration(duration);
    }

    @Override
    public void setBindAdapter(IBindAdapter bindAdapter) {
        delegate.setBindAdapter(bindAdapter);
    }

    @Override
    public void setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
        delegate.setOnPageChangeListener(onPageChangeListener);
    }

    @Override
    public void setOnBannerClickListener(OnBannerClickListener onBannerClickListener) {
        delegate.setOnBannerClickListener(onBannerClickListener);
    }
}
