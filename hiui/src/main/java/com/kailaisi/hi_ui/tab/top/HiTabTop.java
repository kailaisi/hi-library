package com.kailaisi.hi_ui.tab.top;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.kailaisi.hi_ui.R;
import com.kailaisi.hi_ui.tab.common.IHiTab;

public class HiTabTop extends LinearLayout implements IHiTab<HiTabTopInfo<?>> {
    private ImageView tabImageView;
    private TextView tabNameView;
    private HiTabTopInfo<?> tabInfo;
    private View indicator;

    public HiTabTop(Context context) {
        this(context, null);
    }

    public HiTabTop(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HiTabTop(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View root = LayoutInflater.from(getContext()).inflate(R.layout.hi_tab_top, this);
        tabImageView = root.findViewById(R.id.iv_image);
        tabNameView = root.findViewById(R.id.tv_name);
        indicator = root.findViewById(R.id.tab_top_indicator);
    }


    @Override
    public void setHiTabInfo(@NonNull HiTabTopInfo<?> data) {
        this.tabInfo = data;
        inflateView(false, true);
    }

    /**
     * @param selected 是否选中
     * @param init     是否初始化
     */
    private void inflateView(boolean selected, boolean init) {
        if (tabInfo.tabType == HiTabTopInfo.TabType.TEXT) {
            if (init) {
                tabImageView.setVisibility(GONE);
                tabNameView.setVisibility(VISIBLE);
                //设置名字
                if (!TextUtils.isEmpty(tabInfo.name)) {
                    tabNameView.setText(tabInfo.name);
                }
            }
            if (selected) {
                indicator.setVisibility(VISIBLE);
                tabNameView.setTextColor(getTextColor(tabInfo.tintColor));
            } else {
                indicator.setVisibility(GONE);
                tabNameView.setTextColor(getTextColor(tabInfo.defaultColor));
            }
        } else if (tabInfo.tabType == HiTabTopInfo.TabType.BITMAP) {
            if (init) {
                tabImageView.setVisibility(VISIBLE);
                tabNameView.setVisibility(GONE);
                indicator.setVisibility(GONE);
            }
            if (selected) {
                tabImageView.setImageBitmap(null == tabInfo.selectedBitmap ? tabInfo.defaultBitmap : tabInfo.selectedBitmap);
            } else {
                tabImageView.setImageBitmap(tabInfo.defaultBitmap);
            }

        }
    }

    private int getTextColor(Object color) {
        if (color instanceof String) {
            return Color.parseColor((String) color);
        } else {
            return (int) color;
        }
    }


    public ImageView getTabImageView() {
        return tabImageView;
    }


    public TextView getTabNameView() {
        return tabNameView;
    }

    public HiTabTopInfo<?> getHiTabInfo() {
        return tabInfo;
    }

    @Override
    public void resetHeight(int height) {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.height = height;
        setLayoutParams(layoutParams);
        getTabNameView().setVisibility(GONE);
    }

    @Override
    public void onTabSelectedListener(int index, @NonNull HiTabTopInfo<?> preInfo, @NonNull HiTabTopInfo<?> nextInfo) {
        if (preInfo != tabInfo && nextInfo != tabInfo || preInfo == nextInfo) {
            return;
        }
        //将绘制工作交给没给tab去处理
        if (preInfo == tabInfo) {
            inflateView(false, false);
        } else if (nextInfo == tabInfo) {
            inflateView(true, false);
        }

    }
}
