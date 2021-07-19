package com.kailaisi.hi_ui.tab.bottom;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.kailaisi.hi_ui.R;
import com.kailaisi.hi_ui.tab.common.IHiTabLayout;
import com.kailaisi.library.util.HiDisplayUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 描述：含有一个底部导航栏的布局。
 * <p/>作者：kailaisi
 * <br/>创建时间：2021-05-15:13:14
 */
public class HiBottomLayout extends FrameLayout implements IHiTabLayout<HiTabBottom, HiTabBottomInfo<?>> {
    private static final String TAG_TAG_BOTTOM = "TAG_TAG_BOTTOM";
    /**
     * 切换监听
     */
    private List<OnTabSelectedListener<HiTabBottomInfo<?>>> tabSelectedListeners = new ArrayList<>();
    /**
     * 当前选中的
     */
    private HiTabBottomInfo<?> selectedInfo;
    private float bottomAlpha = 1f;
    private float tabBottomHeight = 50;
    private float bottomLineHeight = 0.5f;
    private String bottomLineColor = "#dfe0e1";
    /**
     * bottom信息
     */
    private List<HiTabBottomInfo<?>> infoList;

    public HiBottomLayout(@NonNull Context context) {
        super(context);
    }

    public HiBottomLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HiBottomLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public HiTabBottom findTab(@NonNull HiTabBottomInfo<?> data) {
        ViewGroup ll = findViewWithTag(TAG_TAG_BOTTOM);
        for (int i = 0; i < ll.getChildCount(); i++) {
            View view = ll.getChildAt(i);
            if (view instanceof HiTabBottom) {
                HiTabBottom tab = (HiTabBottom) view;
                if (tab.getHiTabInfo() == data) {
                    return tab;
                }
            }
        }
        return null;
    }

    @Override
    public void addTabSelectedChangedListener(OnTabSelectedListener<HiTabBottomInfo<?>> listener) {
        tabSelectedListeners.add(listener);
    }

    @Override
    public void defaultSelected(@NonNull HiTabBottomInfo<?> defaultInfo) {
        onSelected(defaultInfo);
    }

    @Override
    public void inflateInfo(@NonNull List<HiTabBottomInfo<?>> infoList) {
        if (infoList.isEmpty()) {
            return;
        }
        this.infoList = infoList;
        //移除之前已经添加的View
        for (int i = getChildCount() - 1; i > 0; i++) {
            removeViewAt(i);
        }
        //要移除所有的监听器
        Iterator<OnTabSelectedListener<HiTabBottomInfo<?>>> iterator = tabSelectedListeners.iterator();
        while (iterator.hasNext()) {
            if (iterator.next() instanceof HiTabBottom) {
                iterator.remove();
            }
        }
        selectedInfo = null;
        //设置背景色
        addBackground();
        //添加tab。这里不使用LL,因为有的会单独设置高度，可能会导致LL扩大。
        FrameLayout frameLayout = new FrameLayout(getContext());
        frameLayout.setTag(TAG_TAG_BOTTOM);
        int width = HiDisplayUtils.getScreenWidth() / infoList.size();
        int height = HiDisplayUtils.dip2px(tabBottomHeight);
        for (int i = 0; i < infoList.size(); i++) {
            HiTabBottomInfo<?> info = infoList.get(i);
            LayoutParams params = new LayoutParams(width, height);
            params.gravity = Gravity.BOTTOM;
            params.leftMargin = i * width;
            //每个底部的按钮其实都实现了监听的处理
            HiTabBottom tabBottom = new HiTabBottom(getContext());
            tabSelectedListeners.add(tabBottom);
            tabBottom.setHiTabInfo(info);
            tabBottom.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onSelected(info);
                }
            });
            frameLayout.addView(tabBottom, params);
        }
        LayoutParams flParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        flParams.gravity = Gravity.BOTTOM;
        addBottomLine();
        addView(frameLayout, flParams);
        fixContextView();
    }

    /**
     * 增加底部的线
     */
    private void addBottomLine() {
        View view = new View(getContext());
        view.setBackgroundColor(Color.parseColor(bottomLineColor));
        LayoutParams lineParam = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, HiDisplayUtils.dip2px(bottomLineHeight));
        lineParam.gravity = Gravity.BOTTOM;
        lineParam.bottomMargin = HiDisplayUtils.dip2px(tabBottomHeight - bottomLineHeight);
        addView(view, lineParam);
        view.setAlpha(bottomAlpha);
    }

    private void onSelected(@NotNull HiTabBottomInfo<?> nextInfo) {
        for (OnTabSelectedListener<HiTabBottomInfo<?>> tabSelectedListener : tabSelectedListeners) {
            tabSelectedListener.onTabSelectedListener(infoList.indexOf(nextInfo), selectedInfo, nextInfo);
        }
        this.selectedInfo = nextInfo;
    }

    public void addBackground() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.hi_bottom_layout_bg, null);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, HiDisplayUtils.dip2px(tabBottomHeight));
        params.gravity = Gravity.BOTTOM;
        addView(view, params);
        view.setAlpha(bottomAlpha);
    }

    /**
     * 设置内容区域的padding
     */
    private void fixContextView() {
        if (!(getChildAt(0) instanceof ViewGroup)) {
            return;
        }
        ViewGroup root = (ViewGroup) getChildAt(0);
        ViewGroup target = HiViewUtil.findTypeView(root, RecyclerView.class);
        if (target == null) {
            target = HiViewUtil.findTypeView(root, ScrollView.class);
            if (target == null) {
                target = HiViewUtil.findTypeView(root, AbsListView.class);
            }
        }
        if (target != null) {
            target.setPadding(0, 0, 0, HiDisplayUtils.dip2px(tabBottomHeight));
            target.setClipToPadding(false);
        }
    }

    public void setTabAlpha(float bottomAlpha) {
        this.bottomAlpha = bottomAlpha;
    }

    public void setTabHeight(float tabHeight) {
        this.tabBottomHeight = tabHeight;
    }

    public void setBottomLineHeight(float bottomLineHeight) {
        this.bottomLineHeight = bottomLineHeight;
    }

    public void setBottomLineColor(String bottomLineColor) {
        this.bottomLineColor = bottomLineColor;
    }

    public void resizeHiTabBottomLayout(){
        int width = HiDisplayUtils.getScreenWidth() / infoList.size();
        ViewGroup layout= (ViewGroup) getChildAt(getChildCount()-1);
        int count = layout.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = layout.getChildAt(i);
            FrameLayout.LayoutParams params = (LayoutParams) view.getLayoutParams();
            params.width=width;
            params.leftMargin=i*width;
            view.setLayoutParams(params);
        }
    }
}
