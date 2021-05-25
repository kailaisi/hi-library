package com.kailaisi.hi_ui.banner.core;

import android.content.Context;
import android.widget.Scroller;

/**
 * 描述：可设置滚动时间的Scroller
 * <p/>作者：wu
 * <br/>创建时间：2021-05-25:22:31
 */
public class  HiBannerScroller extends Scroller {

    private int mDuration=1000;
    public HiBannerScroller(Context context,int duration) {
        super(context);
        this.mDuration = duration;
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        super.startScroll(startX, startY, dx, dy,mDuration);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, mDuration);
    }
}
