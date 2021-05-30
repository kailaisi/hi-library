package com.kailaisi.hi_ui.tab.top;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kailaisi.hi_ui.tab.common.IHiTabLayout;
import com.kailaisi.library.util.HiDisplayUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 描述：含有一个顶部导航栏的布局。
 * <p/>作者：kailaisi
 * <br/>创建时间：2021-05-15:13:14
 */
public class HiTopLayout extends HorizontalScrollView implements IHiTabLayout<HiTabTop, HiTabTopInfo<?>> {
    /**
     * 切换监听
     */
    private List<OnTabSelectedListener<HiTabTopInfo<?>>> tabSelectedListeners = new ArrayList<>();
    /**
     * 当前选中的
     */
    private HiTabTopInfo<?> selectedInfo;
    /**
     * Top信息
     */
    private List<HiTabTopInfo<?>> infoList;
    private int tabwidth;


    public HiTopLayout(@NonNull Context context) {
        this(context, null);
    }

    public HiTopLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HiTopLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setVerticalScrollBarEnabled(false);
    }

    @Override
    public HiTabTop findTab(@NonNull HiTabTopInfo<?> data) {
        LinearLayout layout = getRootLayout(false);
        for (int i = 0; i < layout.getChildCount(); i++) {
            View view = layout.getChildAt(i);
            if (view instanceof HiTabTop) {
                HiTabTop tab = (HiTabTop) view;
                if (tab.getHiTabInfo() == data) {
                    return tab;
                }
            }
        }
        return null;
    }

    @Override
    public void addTabSelectedChangedListener(OnTabSelectedListener<HiTabTopInfo<?>> listener) {
        tabSelectedListeners.add(listener);
    }

    @Override
    public void defaultSelected(@NonNull HiTabTopInfo<?> defaultInfo) {
        onSelected(defaultInfo);
    }

    @Override
    public void inflateInfo(@NonNull List<HiTabTopInfo<?>> infoList) {
        if (infoList.isEmpty()) {
            return;
        }
        this.infoList = infoList;
        //移除之前已经添加的View
        for (int i = getChildCount() - 1; i > 0; i++) {
            removeViewAt(i);
        }
        //要移除所有的监听器
        Iterator<OnTabSelectedListener<HiTabTopInfo<?>>> iterator = tabSelectedListeners.iterator();
        while (iterator.hasNext()) {
            if (iterator.next() instanceof HiTabTop) {
                iterator.remove();
            }
        }
        selectedInfo = null;
        LinearLayout linearLayout = getRootLayout(true);
        for (int i = 0; i < infoList.size(); i++) {
            HiTabTopInfo<?> info = infoList.get(i);
            //每个底部的按钮其实都实现了监听的处理
            HiTabTop tabTop = new HiTabTop(getContext());
            tabSelectedListeners.add(tabTop);
            tabTop.setHiTabInfo(info);
            tabTop.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onSelected(info);
                }
            });
            linearLayout.addView(tabTop);
        }
    }

    private int scrollWidth;

    /**
     * @param nextInfo
     */
    private void autoScroll(HiTabTopInfo<?> nextInfo) {
        HiTabTop tab = findTab(nextInfo);
        if (tab == null) {
            return;
        }
        int index = infoList.indexOf(nextInfo);
        int[] loc = new int[2];
        //获取在屏幕中的位置
        tab.getLocationInWindow(loc);
        if (tabwidth == 0) {
            tabwidth = tab.getWidth();
        }
        int scrollWidth=0;
        //点击的tab中心点在屏幕的左侧还是右侧
        if ((loc[0] + tabwidth / 2) > HiDisplayUtils.getScreenWidth(getContext()) / 2) {
            //显示右侧两个按钮的滑动距离
            scrollWidth = rangeScrollWidth(index, 2);
        } else {
            scrollWidth = rangeScrollWidth(index, -2);
        }
        scrollTo(getScrollX() + scrollWidth, 0);
    }

    /**
     * 获取可滚动的范围
     *
     * @param index 从第几个开始
     * @param range 向前向后的范围
     * @return 可滚动的范围
     */
    private int rangeScrollWidth(int index, int range) {
        int scrollWidth=0;
        for (int i = 0; i < Math.abs(range); i++) {
            int next;
            if (range < 0) {
                next = range + i + index;
            } else {
                next = range - i + index;
            }
            //这里其实是过滤，防止超过范围
            if (next >= 0 && next < infoList.size()) {
                if (range < 0) {
                    scrollWidth -= ScrollWidth(next, false);
                } else {
                    scrollWidth += ScrollWidth(next, true);
                }
            }
        }
        return scrollWidth;
    }


    private int ScrollWidth(int index, boolean toRight) {
        HiTabTop tab = findTab(infoList.get(index));
        if (tab == null) {
            return 0;
        }
        //获取在屏幕中显示的view所对应的位置，以左上角为起点
        Rect rect = new Rect();
        tab.getLocalVisibleRect(rect);
        if (toRight) {
            if (rect.right > tabwidth) {//right坐标大于控件的宽度，说明完全没有显示
                return tabwidth;
            } else {//显示了一部分，要减去已显示的宽度
                return tabwidth - rect.right;
            }
        } else {
            if (rect.left <= -tabwidth) {//完全未显示
                return tabwidth;
            } else if (rect.left > 0) {//显示部分
                return rect.left;
            }
            return 0;
        }
    }

    private LinearLayout getRootLayout(boolean clear) {
        LinearLayout rootView = (LinearLayout) getChildAt(0);
        if (rootView == null) {
            rootView = new LinearLayout(getContext());
            rootView.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            addView(rootView, params);
        } else if (clear) {
            rootView.removeAllViews();
        }
        return rootView;
    }


    private void onSelected(@NotNull HiTabTopInfo<?> nextInfo) {
        for (OnTabSelectedListener<HiTabTopInfo<?>> tabSelectedListener : tabSelectedListeners) {
            tabSelectedListener.onTabSelectedListener(infoList.indexOf(nextInfo), selectedInfo, nextInfo);
        }
        this.selectedInfo = nextInfo;
        autoScroll(nextInfo);
    }

}
