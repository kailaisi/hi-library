package com.kailaisi.hi_ui.banner.core;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import org.jetbrains.annotations.NotNull;

import java.io.PipedReader;
import java.util.List;

/**
 * 描述：ViewPager使用的Adapter
 * <p/>作者：wu
 * <br/>创建时间：2021-05-24:22:04
 */
public class HiBannerAdapter extends PagerAdapter {

    private Context mContext;

    private SparseArray<HiBannerViewHolder> mCachedViews = new SparseArray<>();

    private IHiBanner.OnBannerClickListener mBannerClickListener;

    private IBindAdapter mBindAdapter;

    private List<? extends HiBannerMo> models;
    /**
     * 是否启动自动轮播
     */
    private boolean mAutoPlay = true;
    //在非自动轮播状态下是否可以循环切换
    private boolean mLoop = false;
    private int mLayoutResId = -1;

    public HiBannerAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setBannerData(List<? extends HiBannerMo> models) {
        this.models = models;
        //初始化数据
        initCachedView();
        notifyDataSetChanged();
    }

    public void setBannerClickListener(IHiBanner.OnBannerClickListener bannerClickListener) {
        this.mBannerClickListener = bannerClickListener;
    }

    public void setBindAdapter(IBindAdapter bindAdapter) {
        this.mBindAdapter = bindAdapter;
    }

    public void setAutoPlay(boolean autoPlay) {
        this.mAutoPlay = autoPlay;
    }

    public void setLoop(boolean loop) {
        this.mLoop = loop;
    }

    public void setLayoutResId(int layoutResId) {
        this.mLayoutResId = layoutResId;
    }

    /**
     * 获取第一个显示的item的位置
     *
     * @return
     */
    public int getFirstItem() {
        return Integer.MAX_VALUE / 2 - (Integer.MAX_VALUE / 2) % getRealSize();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        int realPos = position;
        if (getRealSize() > 0) {
            realPos = position % getRealSize();
        }
        HiBannerViewHolder viewHolder = mCachedViews.get(realPos);
        if (container.equals(viewHolder.rootView.getParent())) {
            container.removeView(viewHolder.rootView);
        }
        //数据绑定
        onBind(viewHolder, models.get(realPos), realPos);
        if (viewHolder.rootView.getParent() != null) {
            ((ViewGroup) viewHolder.rootView.getParent()).removeView(viewHolder.rootView);
        }
        container.addView(viewHolder.rootView);
        return viewHolder.rootView;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        //让Item每次都会刷新
        return POSITION_NONE;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
    }

    @Override
    public int getCount() {
        return (mAutoPlay || mLoop) ? Integer.MAX_VALUE : getRealSize();
    }

    public int getRealSize() {
        return models == null ? 0 : models.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    protected void onBind(@NotNull final HiBannerViewHolder viewHolder, @NotNull final HiBannerMo mo, int pos) {
        //设置点击事件
        viewHolder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBannerClickListener != null) {
                    mBannerClickListener.onBannerClick(viewHolder, mo, pos);
                }
            }
        });
        //设置数据和视图的绑定，交给外层处理
        if (mBindAdapter != null) {
            mBindAdapter.onBind(viewHolder, mo, pos);
        }
    }

    private void initCachedView() {
        mCachedViews = new SparseArray<>();
        for (int i = 0; i < models.size(); i++) {
            HiBannerViewHolder holder = new HiBannerViewHolder(createView(LayoutInflater.from(mContext), null));
            mCachedViews.put(i, holder);
        }
    }

    private View createView(LayoutInflater inflater, ViewGroup parent) {
        if (mLayoutResId == -1) {
            throw new IllegalArgumentException("you must be set layoutResId first");
        }
        return inflater.inflate(mLayoutResId, parent, false);

    }

    public static class HiBannerViewHolder {
        View rootView;
        private SparseArray<View> viewSparseArray;

        public HiBannerViewHolder(View rootView) {
            this.rootView = rootView;
        }

        public View getRootView() {
            return rootView;
        }

        public <V extends View> V findViewById(int id) {
            if (!(rootView instanceof ViewGroup)) {
                return (V) rootView;
            }
            if (this.viewSparseArray == null) {
                viewSparseArray = new SparseArray<>(1);
            }
            V child = (V) viewSparseArray.get(id);
            if (child == null) {
                child = rootView.findViewById(id);
                viewSparseArray.put(id, child);
            }
            return child;
        }
    }
}
