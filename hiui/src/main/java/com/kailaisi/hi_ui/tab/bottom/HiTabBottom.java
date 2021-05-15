package com.kailaisi.hi_ui.tab.bottom;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.kailaisi.hi_ui.R;
import com.kailaisi.hi_ui.tab.common.IHiTab;

public class HiTabBottom extends RelativeLayout implements IHiTab<HiTabBottomInfo<?>> {
    private ImageView tabImageView;
    private TextView tabIconView;
    private TextView tabNameView;
    private HiTabBottomInfo<?> tabInfo;

    public HiTabBottom(Context context) {
        this(context, null);
    }

    public HiTabBottom(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HiTabBottom(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View root = LayoutInflater.from(getContext()).inflate(R.layout.hi_tab_bottom, this);
        tabImageView = root.findViewById(R.id.iv_image);
        tabIconView = root.findViewById(R.id.tv_icon);
        tabNameView = root.findViewById(R.id.tv_name);
    }


    @Override
    public void setHiTabInfo(@NonNull HiTabBottomInfo<?> data) {
        this.tabInfo = data;
        inflateView(false, true);
    }

    /**
     * @param selected 是否选中
     * @param init     是否初始化
     */
    private void inflateView(boolean selected, boolean init) {
        if (tabInfo.tabType == HiTabBottomInfo.TabType.ICON) {
            if (init) {
                tabImageView.setVisibility(GONE);
                tabIconView.setVisibility(VISIBLE);
                //设置字体库
                Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), tabInfo.iconFont);
                tabIconView.setTypeface(typeface);
                if (!TextUtils.isEmpty(tabInfo.name)) {
                    tabNameView.setText(tabInfo.name);
                }
            }
            if (selected) {
                tabIconView.setText(TextUtils.isEmpty(tabInfo.selectIconName) ? tabInfo.defaultIconName : tabInfo.selectIconName);
                tabNameView.setTextColor(getTextColor(tabInfo.tintColor));
                tabIconView.setTextColor(getTextColor(tabInfo.tintColor));
            } else {

                tabIconView.setText(tabInfo.defaultIconName);
                tabNameView.setTextColor(getTextColor(tabInfo.defaultColor));
                tabIconView.setTextColor(getTextColor(tabInfo.defaultColor));
            }
        } else if (tabInfo.tabType == HiTabBottomInfo.TabType.BITMAP) {
            if (init) {
                tabImageView.setVisibility(VISIBLE);
                tabIconView.setVisibility(GONE);
                if (!TextUtils.isEmpty(tabInfo.name)) {
                    tabNameView.setText(tabInfo.name);
                }
            }
            if (selected) {
                tabImageView.setImageBitmap(null == tabInfo.selectedBitmap ? tabInfo.defaultBitmap : tabInfo.selectedBitmap);
                tabNameView.setTextColor(getTextColor(tabInfo.tintColor));
            } else {
                tabImageView.setImageBitmap(tabInfo.defaultBitmap);
                tabNameView.setTextColor(getTextColor(tabInfo.defaultColor));
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

    public TextView getTabIconView() {
        return tabIconView;
    }

    public TextView getTabNameView() {
        return tabNameView;
    }

    @Override
    public void resetHeight(int height) {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.height = height;
        setLayoutParams(layoutParams);
        getTabNameView().setVisibility(GONE);
    }

    @Override
    public void onTabSelectedListener(int index, @NonNull HiTabBottomInfo<?> preInfo, @NonNull HiTabBottomInfo<?> nextInfo) {
        if (preInfo != tabInfo && nextInfo != tabInfo || preInfo == nextInfo) {
            return;
        }
        if (preInfo == tabInfo) {
            inflateView(false, false);
        } else if (nextInfo == tabInfo) {
            inflateView(true, false);
        }

    }


}
