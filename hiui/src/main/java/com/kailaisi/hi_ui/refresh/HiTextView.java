package com.kailaisi.hi_ui.refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kailaisi.hi_ui.R;

/**
 * 描述：
 * <p/>作者：wu
 * <br/>创建时间：2021-05-19:22:20
 */
public class HiTextView extends HiOverView {

    private ImageView rote;
    private TextView text;

    public HiTextView(@NonNull Context context) {
        super(context);
    }

    public HiTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HiTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.hi_refresh_overview, this, true);
        rote = findViewById(R.id.iv_rotate);
        text = findViewById(R.id.text);
    }

    @Override
    protected void onScroll(int scrollY, int pullRefreshHeight) {

    }

    @Override
    protected void onVisible() {
        text.setText("下拉刷新");
    }

    @Override
    public void onOver() {
        text.setText("松开刷新");
    }

    @Override
    public void onRefresh() {
        text.setText("正在刷新");
    }

    @Override
    public void onFinish() {

    }
}
