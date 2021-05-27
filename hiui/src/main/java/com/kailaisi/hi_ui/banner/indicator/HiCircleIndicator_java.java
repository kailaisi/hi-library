package com.kailaisi.hi_ui.banner.indicator;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kailaisi.hi_ui.R;
import com.kailaisi.hi_ui.banner.core.HiIndicator;
import com.kailaisi.library.util.HiDisplayUtils;

/**
 * 默认的圆形指示器
 */
public class HiCircleIndicator_java extends FrameLayout implements HiIndicator<FrameLayout> {

    public static final int VWC = ViewGroup.LayoutParams.WRAP_CONTENT;
    private int leftRightPadding;
    private int topBottomPadding;

    private @DrawableRes
    int pointNormal = R.drawable.shap_point_normal;
    private @DrawableRes
    int pointSelected = R.drawable.shap_point_selected;

    public HiCircleIndicator_java(@NonNull Context context) {
        this(context, null);
    }

    public HiCircleIndicator_java(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HiCircleIndicator_java(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        leftRightPadding = HiDisplayUtils.dip2px(getContext(), 5);
        topBottomPadding = HiDisplayUtils.dip2px(getContext(), 15);
    }

    @Override
    public FrameLayout get() {
        return this;
    }

    @Override
    public void onInflate(int count) {
        removeAllViews();
        if (count <= 0) {
            return;
        }
        LinearLayout group = new LinearLayout(getContext());
        group.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(VWC, VWC);
        params.gravity = Gravity.CENTER_VERTICAL;
        params.setMargins(leftRightPadding, topBottomPadding, leftRightPadding, topBottomPadding);
        for (int i = 0; i < count; i++) {
            ImageView imageView = new ImageView(getContext());
            imageView.setLayoutParams(params);
            if (i == 0) {
                imageView.setImageResource(pointSelected);
            } else {
                imageView.setImageResource(pointNormal);
            }
            group.addView(imageView);
        }
        LayoutParams groupParam = new LayoutParams(VWC, VWC);
        groupParam.gravity = Gravity.CENTER | Gravity.BOTTOM;
        addView(group, groupParam);
    }

    @Override
    public void onPointChange(int current, int count) {
        ViewGroup viewGroup = (ViewGroup) getChildAt(0);
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            ImageView child = (ImageView) viewGroup.getChildAt(i);
            if (i == current) {
                child.setImageResource(pointSelected);
            } else {
                child.setImageResource(pointNormal);
            }
            child.requestLayout();
        }
    }
}
