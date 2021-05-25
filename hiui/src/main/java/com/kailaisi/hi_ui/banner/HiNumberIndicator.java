package com.kailaisi.hi_ui.banner;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kailaisi.hi_ui.R;
import com.kailaisi.hi_ui.banner.core.HiIndicator;
import com.kailaisi.library.util.HiDisplayUtils;

/**
 * 默认的圆形指示器
 */
public class HiNumberIndicator extends FrameLayout implements HiIndicator<FrameLayout> {

    public static final int VWC = ViewGroup.LayoutParams.WRAP_CONTENT;
    private int leftRightPadding;
    private int topBottomPadding;

    public HiNumberIndicator(@NonNull Context context) {
        this(context, null);
    }

    public HiNumberIndicator(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HiNumberIndicator(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
        params.setMargins(leftRightPadding, leftRightPadding, leftRightPadding, leftRightPadding);
        TextView view = new TextView(getContext());
        view.setText("0/" + count);
        group.addView(view, params);
        LayoutParams groupParam = new LayoutParams(VWC, VWC);
        groupParam.gravity = Gravity.END | Gravity.BOTTOM;
        addView(group, groupParam);
    }

    @Override
    public void onPointChange(int current, int count) {
        ViewGroup viewGroup = (ViewGroup) getChildAt(0);
        TextView child = (TextView) viewGroup.getChildAt(0);
        child.setText(current + "/" + count);
    }
}
