package com.kailaisi.hi_ui.banner.core;

import androidx.annotation.LayoutRes;
import androidx.viewpager.widget.ViewPager;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * 描述：通用接口
 * <p/>作者：kailaisi
 * <br/>创建时间：2021-05-24:21:46
 */
public interface IHiBanner {
    void setBannerData(@LayoutRes int layoutId, @NotNull List<? extends HiBannerMo> models);

    void setBannerData(@NotNull List<? extends HiBannerMo> models);

    void setHaIndicator(HiIndicator<?> haIndicator);

    void setAutoPlay(boolean autoPlay);

    void setLoop(boolean loop);

    void setIntervalTime(int intervalTime);

    void setScrollDuration(int duration);

    void setBindAdapter(IBindAdapter bindAdapter);

    void setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener);

    void setOnBannerClickListener(OnBannerClickListener onBannerClickListener);

    interface OnBannerClickListener {
        void onBannerClick(@NotNull HiBannerAdapter.HiBannerViewHolder viewHolder, @NotNull HiBannerMo bannerMo, int pos);
    }
}
